package com.costumemanagement.costumerental.service.impl;

import com.costumemanagement.costumerental.entity.KichThuoc;
import com.costumemanagement.costumerental.repository.KichThuocRepository;
import com.costumemanagement.costumerental.service.KichThuocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KichThuocServiceImpl implements KichThuocService {

    private static final Logger logger = LoggerFactory.getLogger(KichThuocServiceImpl.class);

    private final KichThuocRepository kichThuocRepo;

    public KichThuocServiceImpl(KichThuocRepository kichThuocRepo) {
        this.kichThuocRepo = kichThuocRepo;
    }

    @Override
    public List<KichThuoc> findAll() {
        return kichThuocRepo.findAll();
    }

    @Override
    public KichThuoc findById(Long id) {
        return kichThuocRepo.findById(id).orElse(null);
    }

    @Override
    public KichThuoc save(KichThuoc kichThuoc) {
        logger.info("Tạo kích thước mới: {}", kichThuoc.getKichThuoc());
        return kichThuocRepo.save(kichThuoc);
    }

    @Override
    public KichThuoc update(Long id, KichThuoc newData) {
        KichThuoc existing = findById(id);
        if (existing == null) {
            logger.error("Không tìm thấy kích thước id: {}", id);
            return null;
        }

        existing.setKichThuoc(newData.getKichThuoc());
        existing.setMoTa(newData.getMoTa());

        logger.info("Cập nhật kích thước id: {}", id);
        return kichThuocRepo.save(existing);
    }

    @Override
    public boolean delete(Long id) {
        if (!kichThuocRepo.existsById(id)) {
            return false;
        }
        kichThuocRepo.deleteById(id);
        logger.info("Đã xóa kích thước id: {}", id);
        return true;
    }
}