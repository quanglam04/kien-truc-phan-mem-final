package com.costumemanagement.costumerental.service.impl;

import com.costumemanagement.costumerental.constant.CostumeStatus;
import com.costumemanagement.costumerental.entity.TrangPhuc;
import com.costumemanagement.costumerental.entity.KichThuocTrangPhuc;
import com.costumemanagement.costumerental.entity.KichThuoc;
import com.costumemanagement.costumerental.repository.TrangPhucRepository;
import com.costumemanagement.costumerental.repository.KichThuocTrangPhucRepository;
import com.costumemanagement.costumerental.repository.KichThuocRepository;
import com.costumemanagement.costumerental.service.TrangPhucService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrangPhucServiceImpl implements TrangPhucService {

    private static final Logger logger = LoggerFactory.getLogger(TrangPhucServiceImpl.class);

    private final TrangPhucRepository costumeRepo;
    private final KichThuocTrangPhucRepository costumeSizeRepo;
    private final KichThuocRepository sizeRepo;

    @Value("${app.prefix.costume}")
    private String prefix;

    @Value("${app.format.costume}")
    private String format;

    private int autoId = 1;

    public TrangPhucServiceImpl(TrangPhucRepository costumeRepo,
                               KichThuocTrangPhucRepository costumeSizeRepo,
                               KichThuocRepository sizeRepo) {
        this.costumeRepo = costumeRepo;
        this.costumeSizeRepo = costumeSizeRepo;
        this.sizeRepo = sizeRepo;
        this.autoId = (int) costumeRepo.count() + 1;
    }

    @Override
    public List<TrangPhuc> findAll() {
        return costumeRepo.findAll();
    }

    @Override
    public TrangPhuc findById(String id) {
        return costumeRepo.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public TrangPhuc save(TrangPhuc trangPhuc) {
        String newId = prefix + String.format(format, autoId++);
        trangPhuc.setMaTrangPhuc(newId);
        if (trangPhuc.getTrangThai() == null) {
            trangPhuc.setTrangThai(CostumeStatus.CON_HANG);
        }

        // Lưu trang phục trước
        TrangPhuc saved = costumeRepo.save(trangPhuc);

        // Gán liên kết ngược cho từng CostumeSize
        if (saved.getKichThuocs() != null) {
            for (KichThuocTrangPhuc cs : saved.getKichThuocs()) {
                cs.setTrangPhuc(saved);
            }
            costumeSizeRepo.saveAll(saved.getKichThuocs());
        }

        logger.info("Tạo trang phục mới: {}", newId);
        return saved;
    }

    @Override
    @Transactional
    public TrangPhuc update(String id, TrangPhuc newData) {
        TrangPhuc existing = findById(id);
        if (existing == null) {
            logger.error("Không tìm thấy trang phục: {}", id);
            return null;
        }

        existing.setTenTrangPhuc(newData.getTenTrangPhuc());
        existing.setLoaiTrangPhuc(newData.getLoaiTrangPhuc());
        existing.setMauSac(newData.getMauSac());
        existing.setGiaThue(newData.getGiaThue());
        existing.setGiaGoc(newData.getGiaGoc());
        existing.setTrangThai(newData.getTrangThai());

        // Cập nhật danh sách size nếu có truyền vào
        if (newData.getKichThuocs() != null) {
            // Xóa các size cũ
            costumeSizeRepo.deleteAll(existing.getKichThuocs());
            // Gán liên kết và lưu size mới
            for (KichThuocTrangPhuc cs : newData.getKichThuocs()) {
                cs.setTrangPhuc(existing);
            }
            existing.setKichThuocs(newData.getKichThuocs());
        }

        logger.info("Cập nhật trang phục: {}", id);
        return costumeRepo.save(existing);
    }

    @Override
    @Transactional
    public boolean delete(String id) {
        if (!costumeRepo.existsById(id)) return false;
        costumeRepo.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public KichThuocTrangPhuc updateQuantityBySize(String maTrangPhuc, Long idKichThuoc, int delta) {
        TrangPhuc tp = findById(maTrangPhuc);
        if (tp == null) {
            logger.error("Không tìm thấy trang phục: {}", maTrangPhuc);
            return null;
        }

        KichThuoc size = sizeRepo.findById(idKichThuoc).orElse(null);
        if (size == null) {
            logger.error("Không tìm thấy kích thước id: {}", idKichThuoc);
            return null;
        }

        KichThuocTrangPhuc cs = costumeSizeRepo.findByTrangPhucAndKichThuoc(tp, size)
                .orElseGet(() -> new KichThuocTrangPhuc(tp, size, 0));

        cs.setSoLuong(cs.getSoLuong() + delta);
        KichThuocTrangPhuc saved = costumeSizeRepo.save(cs);

        logger.info("Cập nhật số lượng trang phục {} - size {}: delta={}, mới={}",
                maTrangPhuc, size.getKichThuoc(), delta, saved.getSoLuong());
        return saved;
    }

    @Override
    public List<KichThuocTrangPhuc> getSizeQuantities(String maTrangPhuc) {
        TrangPhuc tp = findById(maTrangPhuc);
        if (tp == null) return List.of();
        return costumeSizeRepo.findByTrangPhuc(tp);
    }
}