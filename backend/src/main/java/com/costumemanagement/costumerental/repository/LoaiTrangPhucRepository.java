package com.costumemanagement.costumerental.repository;

import com.costumemanagement.costumerental.entity.LoaiTrangPhuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoaiTrangPhucRepository extends JpaRepository<LoaiTrangPhuc, Long> {
}