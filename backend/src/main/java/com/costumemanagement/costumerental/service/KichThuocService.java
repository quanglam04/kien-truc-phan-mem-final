package com.costumemanagement.costumerental.service;

import com.costumemanagement.costumerental.entity.KichThuoc;
import java.util.List;

public interface KichThuocService {
    List<KichThuoc> findAll();
    KichThuoc findById(Long id);
    KichThuoc save(KichThuoc kichThuoc);
    KichThuoc update(Long id, KichThuoc kichThuoc);
    boolean delete(Long id);
}