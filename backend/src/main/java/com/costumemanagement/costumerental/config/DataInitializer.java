package com.costumemanagement.costumerental.config;

import com.costumemanagement.costumerental.constant.CostumeStatus;
import com.costumemanagement.costumerental.constant.EmployeeRole;
import com.costumemanagement.costumerental.entity.*;
import com.costumemanagement.costumerental.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Bean
    CommandLineRunner initData(
            TrangPhucRepository costumeRepo,
            NhaCungCapRepository supplierRepo,
            NhanVienRepository employeeRepository,
            LoaiTrangPhucRepository loaiRepo,
            KichThuocRepository kichThuocRepo,
            KichThuocTrangPhucRepository costumeSizeRepo) {

        return args -> {
            // Loai Trang Phuc
            if (loaiRepo.count() == 0) {
                LoaiTrangPhuc aoDai = new LoaiTrangPhuc();
                aoDai.setLoaiTrangPhuc("Ao dai");
                aoDai.setMoTa("Áo dài truyền thống");
                loaiRepo.save(aoDai);

                LoaiTrangPhuc vest = new LoaiTrangPhuc();
                vest.setLoaiTrangPhuc("Vest");
                vest.setMoTa("Vest công sở");
                loaiRepo.save(vest);

                LoaiTrangPhuc dam = new LoaiTrangPhuc();
                dam.setLoaiTrangPhuc("Dam");
                dam.setMoTa("Đầm dự tiệc");
                loaiRepo.save(dam);

                logger.info("Đã thêm LoaiTrangPhuc mẫu");
            }

            // Kich Thuoc
            if (kichThuocRepo.count() == 0) {
                List.of("S", "M", "L", "XL").forEach(size -> {
                    KichThuoc k = new KichThuoc();
                    k.setKichThuoc(size);
                    k.setMoTa("Size " + size);
                    kichThuocRepo.save(k);
                });
                logger.info("Đã thêm KichThuoc mẫu");
            }

            // Trang Phuc
            if (costumeRepo.count() == 0) {
                LoaiTrangPhuc aoDai = loaiRepo.findAll().stream()
                        .filter(l -> "Ao dai".equals(l.getLoaiTrangPhuc())).findFirst().orElse(null);
                LoaiTrangPhuc vest = loaiRepo.findAll().stream()
                        .filter(l -> "Vest".equals(l.getLoaiTrangPhuc())).findFirst().orElse(null);

                List<KichThuoc> sizes = kichThuocRepo.findAll();

                // Trang phuc 1
                TrangPhuc tp1 = new TrangPhuc();
                tp1.setMaTrangPhuc("TP001");
                tp1.setTenTrangPhuc("Ao dai truyen thong");
                tp1.setLoaiTrangPhuc(aoDai);
                tp1.setMauSac("Do");
                tp1.setGiaThue(200000);
                tp1.setGiaGoc(1500000);
                tp1.setTrangThai(CostumeStatus.CON_HANG);
                costumeRepo.save(tp1);

                // Tạo CostumeSize cho tp1: mỗi size 5 cái
                for (KichThuoc s : sizes) {
                    costumeSizeRepo.save(new KichThuocTrangPhuc(tp1, s, 5));
                }

                // Trang phuc 2
                TrangPhuc tp2 = new TrangPhuc();
                tp2.setMaTrangPhuc("TP002");
                tp2.setTenTrangPhuc("Vest nam cong so");
                tp2.setLoaiTrangPhuc(vest);
                tp2.setMauSac("Den");
                tp2.setGiaThue(300000);
                tp2.setGiaGoc(2500000);
                tp2.setTrangThai(CostumeStatus.CON_HANG);
                costumeRepo.save(tp2);

                // Tạo CostumeSize cho tp2: S=2, M=3, L=2, XL=1
                int[] qtys = {2, 3, 2, 1};
                for (int i = 0; i < sizes.size(); i++) {
                    costumeSizeRepo.save(new KichThuocTrangPhuc(tp2, sizes.get(i), qtys[i]));
                }

                logger.info("Đã thêm TrangPhuc mẫu với số lượng theo từng size");
            }

            if (supplierRepo.count() == 0) {
                NhaCungCap ncc1 = new NhaCungCap();
                ncc1.setMaNCC("NCC01");
                ncc1.setTenNCC("Cong ty May Viet Tien");
                ncc1.setEmail("viettien@email.com");
                ncc1.setSoDienThoai("0901234567");
                ncc1.setDiaChi("TP.HCM");
                ncc1.setGhiChu("NCC lau nam");
                supplierRepo.save(ncc1);

                NhaCungCap ncc2 = new NhaCungCap();
                ncc2.setMaNCC("NCC02");
                ncc2.setTenNCC("Xuong may Hoang Gia");
                ncc2.setEmail("hoanggia@email.com");
                ncc2.setSoDienThoai("0912345678");
                ncc2.setDiaChi("Ha Noi");
                ncc2.setGhiChu("Chuyen vest");
                supplierRepo.save(ncc2);

                logger.info("Da them 2 NCC mau vao database");
            }

            if (employeeRepository.count() == 0) {
            NhanVien nv1 = new NhanVien();
            nv1.setMaNhanVien("NV01");
            nv1.setViTri(EmployeeRole.NHAN_VIEN_KHO);
            nv1.setHoTen("Nguyen Van A");
            nv1.setUsername("nv01");
            nv1.setPassword("123456");
            nv1.setEmail("nva@email.com");
            nv1.setSoDienThoai("0901111111");
            nv1.setDiaChi("TP.HCM");
            nv1.setDob(LocalDate.of(1995, 6, 15));
            employeeRepository.save(nv1);

            NhanVien nv2 = new NhanVien();
            nv2.setMaNhanVien("NV02");
            nv2.setViTri(EmployeeRole.NHAN_VIEN_KHO);
            nv2.setHoTen("Tran Van B");
            nv2.setUsername("nv02");
            nv2.setPassword("123456");
            nv2.setEmail("tvb@email.com");
            nv2.setSoDienThoai("0902222222");
            nv2.setDiaChi("Ha Noi");
            nv2.setDob(LocalDate.of(1998, 3, 20));
            employeeRepository.save(nv2);

            logger.info("Da them 2 nhan vien mau vao database");
        }
        };
    }
}