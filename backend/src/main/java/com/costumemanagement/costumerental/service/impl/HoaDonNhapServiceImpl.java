package com.costumemanagement.costumerental.service.impl;

import com.costumemanagement.costumerental.constant.PaymentMethod;
import com.costumemanagement.costumerental.constant.PaymentStatus;
import com.costumemanagement.costumerental.entity.*;
import com.costumemanagement.costumerental.repository.TrangPhucRepository;
import com.costumemanagement.costumerental.repository.NhanVienRepository;
import com.costumemanagement.costumerental.repository.HoaDonNhapRepository;
import com.costumemanagement.costumerental.repository.NhaCungCapRepository;
import com.costumemanagement.costumerental.service.TrangPhucService;
import com.costumemanagement.costumerental.service.HoaDonNhapService;
import com.costumemanagement.costumerental.service.payment.ThanhToanFactory;
import com.costumemanagement.costumerental.service.payment.ThanhToanProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class HoaDonNhapServiceImpl implements HoaDonNhapService {

    private static final Logger logger = LoggerFactory.getLogger(HoaDonNhapServiceImpl.class);

    private final HoaDonNhapRepository importRepo;
    private final NhaCungCapRepository supplierRepo;
    private final TrangPhucRepository costumeRepo;
    private final ThanhToanFactory paymentFactory;
    private final NhanVienRepository employeeRepository;
    private final TrangPhucService costumeService;

    @Value("${app.prefix.import}")
    private String prefix;

    @Value("${app.format.import}")
    private String format;

    private int autoId = 1;

    public HoaDonNhapServiceImpl(HoaDonNhapRepository importRepo,
                             NhaCungCapRepository supplierRepo,
                             TrangPhucRepository costumeRepo,
                             ThanhToanFactory paymentFactory,
                             NhanVienRepository employeeRepository,
                             TrangPhucService costumeService) {
        this.importRepo = importRepo;
        this.supplierRepo = supplierRepo;
        this.costumeRepo = costumeRepo;
        this.paymentFactory = paymentFactory;
        this.employeeRepository = employeeRepository;
        this.costumeService = costumeService;
        this.autoId = (int) importRepo.count() + 1;
    }

    @Override
    public List<HoaDonNhap> findAll() {
        return importRepo.findAll();
    }

    @Override
    public HoaDonNhap findById(String id) {
        return importRepo.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public HoaDonNhap createImport(HoaDonNhap hoaDonNhap) {
        logger.info("Tao hoa don nhap - NCC: {}", 
            hoaDonNhap.getNhaCungCap() != null ? hoaDonNhap.getNhaCungCap().getMaNCC() : null);

        // Kiểm tra Nhà cung cấp
        if (hoaDonNhap.getNhaCungCap() == null || hoaDonNhap.getNhaCungCap().getMaNCC() == null) {
            return null;
        }

        NhaCungCap ncc = supplierRepo.findById(hoaDonNhap.getNhaCungCap().getMaNCC()).orElse(null);
        if (ncc == null) {
            logger.error("Khong tim thay NCC: {}", hoaDonNhap.getNhaCungCap().getMaNCC());
            return null;
        }
        hoaDonNhap.setNhaCungCap(ncc);

        // Kiểm tra Nhân viên (nếu có)
        if (hoaDonNhap.getNhanVien() != null && hoaDonNhap.getNhanVien().getMaNhanVien() != null) {
            NhanVien nv = employeeRepository.findById(hoaDonNhap.getNhanVien().getMaNhanVien()).orElse(null);
            if (nv != null) {
                hoaDonNhap.setNhanVien(nv);
            }
        }

        // Kiểm tra và gán chi tiết
        float tongTien = 0;
        if (hoaDonNhap.getChiTietHDNhap() != null) {
            for (ChiTietHoaDonNhap ct : hoaDonNhap.getChiTietHDNhap()) {
                if (ct.getTrangPhuc() == null || ct.getTrangPhuc().getMaTrangPhuc() == null) {
                    return null;
                }

                TrangPhuc tp = costumeRepo.findById(ct.getTrangPhuc().getMaTrangPhuc()).orElse(null);
                if (tp == null) {
                    logger.error("Khong tim thay trang phuc: {}", ct.getTrangPhuc().getMaTrangPhuc());
                    return null;
                }

                ct.setTrangPhuc(tp);
                ct.setThanhTien(ct.getSoLuong() * ct.getDonGiaNhap());
                tongTien += ct.getThanhTien();
                ct.setHoaDonNhap(hoaDonNhap);   // Liên kết ngược
            }
        }

        // Tạo mã hóa đơn mới
        String maHD = prefix + String.format(format, autoId++);
        hoaDonNhap.setMaHDNhap(maHD);
        hoaDonNhap.setTongTien(tongTien);
        hoaDonNhap.setNgayNhap(LocalDate.now());
        hoaDonNhap.setTrangThaiThanhToan(PaymentStatus.CHO_THANH_TOAN);

        HoaDonNhap saved = importRepo.save(hoaDonNhap);
        logger.info("Da tao hoa don: {} - Tong tien: {}", saved.getMaHDNhap(), saved.getTongTien());
        return saved;
    }

    @Override
    @Transactional
    public HoaDonNhap payImport(String id, String phuongThucStr) {
        logger.info("Bat dau thanh toan hoa don: {}", id);

        HoaDonNhap hoaDon = findById(id);
        if (hoaDon == null) {
            return null;
        }

        if (PaymentStatus.DA_THANH_TOAN.equals(hoaDon.getTrangThaiThanhToan())) {
            logger.warn("Hoa don {} da thanh toan roi", id);
            return hoaDon;
        }

        try {
            PaymentMethod method = PaymentMethod.valueOf(phuongThucStr);
            ThanhToanProcessor processor = paymentFactory.getProcessor(method);

            boolean success = processor.pay(hoaDon);

            if (success) {
                hoaDon.setTrangThaiThanhToan(PaymentStatus.DA_THANH_TOAN);
                hoaDon.setPhuongThucThanhToan(method);
                importRepo.save(hoaDon);

                // Cập nhật số lượng tồn kho theo từng size
                for (ChiTietHoaDonNhap ct : hoaDon.getChiTietHDNhap()) {
                    TrangPhuc tp = ct.getTrangPhuc();
                    // Nếu chi tiết có ghi rõ size thì cập nhật theo size đó,
                    // ngược lại cộng đều vào tất cả các size hiện có
                    if (ct.getKichThuoc() != null) {
                        costumeService.updateQuantityBySize(
                                tp.getMaTrangPhuc(),
                                ct.getKichThuoc().getId(),
                                ct.getSoLuong());
                        logger.info("Cap nhat so luong {} - size {}: +{}",
                                tp.getMaTrangPhuc(), ct.getKichThuoc().getKichThuoc(), ct.getSoLuong());
                    } else {
                        // Không chỉ định size: cộng đều vào các size đang có
                        costumeService.getSizeQuantities(tp.getMaTrangPhuc()).forEach(cs ->
                                costumeService.updateQuantityBySize(
                                        tp.getMaTrangPhuc(),
                                        cs.getKichThuoc().getId(),
                                        ct.getSoLuong()));
                        logger.info("Cap nhat so luong {} (tat ca size): +{}",
                                tp.getMaTrangPhuc(), ct.getSoLuong());
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Phuong thuc thanh toan khong hop le: " + phuongThucStr);
        }

        return hoaDon;
    }
}