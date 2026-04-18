package com.costumemanagement.costumerental.service.impl;

import com.costumemanagement.costumerental.entity.LoaiTrangPhuc;
import com.costumemanagement.costumerental.repository.LoaiTrangPhucRepository;
import com.costumemanagement.costumerental.service.LoaiTrangPhucService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoaiTrangPhucServiceImpl implements LoaiTrangPhucService {

    private static final Logger logger = LoggerFactory.getLogger(LoaiTrangPhucServiceImpl.class);

    private final LoaiTrangPhucRepository loaiRepo;

    public LoaiTrangPhucServiceImpl(LoaiTrangPhucRepository loaiRepo) {
        this.loaiRepo = loaiRepo;
    }

    @Override
    public List<LoaiTrangPhuc> findAll() {
        return loaiRepo.findAll();
    }

    @Override
    public LoaiTrangPhuc findById(Long id) {
        return loaiRepo.findById(id).orElse(null);
    }

    @Override
    public LoaiTrangPhuc save(LoaiTrangPhuc loai) {
        logger.info("Tạo loại trang phục mới: {}", loai.getLoaiTrangPhuc());
        return loaiRepo.save(loai);
    }

    @Override
    public LoaiTrangPhuc update(Long id, LoaiTrangPhuc newData) {
        LoaiTrangPhuc existing = findById(id);
        if (existing == null) {
            logger.error("Không tìm thấy loại trang phục id: {}", id);
            return null;
        }

        existing.setLoaiTrangPhuc(newData.getLoaiTrangPhuc());
        existing.setMoTa(newData.getMoTa());

        logger.info("Cập nhật loại trang phục id: {}", id);
        return loaiRepo.save(existing);
    }

    @Override
    public boolean delete(Long id) {
        if (!loaiRepo.existsById(id)) {
            return false;
        }
        loaiRepo.deleteById(id);
        logger.info("Đã xóa loại trang phục id: {}", id);
        return true;
    }
}