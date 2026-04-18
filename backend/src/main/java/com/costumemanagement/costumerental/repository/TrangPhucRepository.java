package com.costumemanagement.costumerental.repository;

import com.costumemanagement.costumerental.constant.CostumeStatus;
import com.costumemanagement.costumerental.entity.TrangPhuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrangPhucRepository extends JpaRepository<TrangPhuc, String> {

    List<TrangPhuc> findByLoaiTrangPhuc_LoaiTrangPhuc(String loaiTrangPhuc);

    List<TrangPhuc> findByTrangThai(CostumeStatus trangThai);
}
