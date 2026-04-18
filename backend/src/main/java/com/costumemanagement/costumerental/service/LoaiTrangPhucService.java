package com.costumemanagement.costumerental.service;

import com.costumemanagement.costumerental.entity.LoaiTrangPhuc;
import java.util.List;

public interface LoaiTrangPhucService {
    List<LoaiTrangPhuc> findAll();
    LoaiTrangPhuc findById(Long id);
    LoaiTrangPhuc save(LoaiTrangPhuc loai);
    LoaiTrangPhuc update(Long id, LoaiTrangPhuc loai);
    boolean delete(Long id);
}