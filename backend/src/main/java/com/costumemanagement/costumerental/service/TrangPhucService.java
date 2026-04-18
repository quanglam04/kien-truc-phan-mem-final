package com.costumemanagement.costumerental.service;

import com.costumemanagement.costumerental.entity.TrangPhuc;
import com.costumemanagement.costumerental.entity.KichThuocTrangPhuc;

import java.util.List;

public interface TrangPhucService {

    List<TrangPhuc> findAll();
    TrangPhuc findById(String id);
    TrangPhuc save(TrangPhuc trangPhuc);
    TrangPhuc update(String id, TrangPhuc trangPhuc);
    boolean delete(String id);

    /**
     * Cập nhật số lượng theo size cụ thể (dùng khi nhập hàng).
     * @param maTrangPhuc mã trang phục
     * @param maKichThuoc id của size
     * @param delta số lượng thêm vào (dương = nhập thêm)
     */
    KichThuocTrangPhuc updateQuantityBySize(String maTrangPhuc, Long maKichThuoc, int delta);

    /**
     * Lấy danh sách số lượng theo từng size của một trang phục
     */
    List<KichThuocTrangPhuc> getSizeQuantities(String maTrangPhuc);
}
