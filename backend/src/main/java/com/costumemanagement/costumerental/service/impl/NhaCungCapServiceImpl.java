package com.costumemanagement.costumerental.service.impl;

import com.costumemanagement.costumerental.entity.ChiTietHoaDonNhap;
import com.costumemanagement.costumerental.entity.HoaDonNhap;
import com.costumemanagement.costumerental.entity.NhaCungCap;
import com.costumemanagement.costumerental.repository.HoaDonNhapRepository;
import com.costumemanagement.costumerental.repository.NhaCungCapRepository;
import com.costumemanagement.costumerental.service.NhaCungCapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NhaCungCapServiceImpl implements NhaCungCapService {

    private static final Logger logger = LoggerFactory.getLogger(NhaCungCapServiceImpl.class);

    private final NhaCungCapRepository supplierRepo;
    private final HoaDonNhapRepository importRepo;

    @Value("${app.prefix.supplier}")
    private String prefix;

    @Value("${app.format.supplier}")
    private String format;

    private int autoId = 1;

    public NhaCungCapServiceImpl(NhaCungCapRepository supplierRepo, HoaDonNhapRepository importRepo) {
        this.supplierRepo = supplierRepo;
        this.importRepo = importRepo;
        this.autoId = (int) supplierRepo.count() + 1;
    }

    @Override
    public List<NhaCungCap> findAll() {
        return supplierRepo.findAll();
    }

    @Override
    public NhaCungCap findById(String id) {
        return supplierRepo.findById(id).orElse(null);
    }

    @Override
    public NhaCungCap save(NhaCungCap ncc) {
        String newId = prefix + String.format(format, autoId++);
        ncc.setMaNCC(newId);
        
        logger.info("Tao NCC moi: {} - {}", newId, ncc.getTenNCC());
        return supplierRepo.save(ncc);
    }

    @Override
    public NhaCungCap update(String id, NhaCungCap newData) {
        NhaCungCap existing = findById(id);
        if (existing == null) {
            logger.error("Khong tim thay NCC: {}", id);
            return null;
        }

        existing.setTenNCC(newData.getTenNCC());
        existing.setEmail(newData.getEmail());
        existing.setSoDienThoai(newData.getSoDienThoai());
        existing.setDiaChi(newData.getDiaChi());
        existing.setGhiChu(newData.getGhiChu());

        logger.info("Cap nhat NCC: {}", id);
        return supplierRepo.save(existing);
    }

    @Override
    public boolean delete(String id) {
        if (!supplierRepo.existsById(id)) {
            return false;
        }
        supplierRepo.deleteById(id);
        return true;
    }

    @Override
    public List<Map<String, Object>> getStats() {
        logger.info("Thong ke tat ca NCC theo luong hang nhap");
        List<NhaCungCap> allNCC = supplierRepo.findAll();
        List<Map<String, Object>> statsList = new ArrayList<>();

        for (NhaCungCap ncc : allNCC) {
            statsList.add(buildStatsMap(ncc));
        }
        return statsList;
    }

    @Override
    public Map<String, Object> getStatsById(String maNCC) {
        logger.info("Thong ke NCC: {}", maNCC);
        NhaCungCap ncc = findById(maNCC);
        if (ncc == null) {
            return null;
        }
        return buildStatsMap(ncc);
    }

    private Map<String, Object> buildStatsMap(NhaCungCap ncc) {
        List<HoaDonNhap> hoaDons = importRepo.findByNhaCungCap(ncc);

        int tongSoLuong = 0;
        float tongGiaTri = 0;

        for (HoaDonNhap hd : hoaDons) {
            if (hd.getChiTietHDNhap() != null) {
                for (ChiTietHoaDonNhap ct : hd.getChiTietHDNhap()) {
                    tongSoLuong += ct.getSoLuong();
                    tongGiaTri += ct.getThanhTien();
                }
            }
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("maNCC", ncc.getMaNCC());
        stats.put("tenNCC", ncc.getTenNCC());
        stats.put("soLuongHang", tongSoLuong);
        stats.put("tongGiaTri", tongGiaTri);
        stats.put("soHoaDon", hoaDons.size());

        logger.info("NCC {} - So luong hang: {} - Tong gia tri: {} - So hoa don: {}",
                ncc.getMaNCC(), tongSoLuong, tongGiaTri, hoaDons.size());

        return stats;
    }
}