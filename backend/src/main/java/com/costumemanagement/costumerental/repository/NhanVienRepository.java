package com.costumemanagement.costumerental.repository;

import com.costumemanagement.costumerental.constant.EmployeeRole;
import com.costumemanagement.costumerental.entity.NhanVien;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, String> {
    List<NhanVien> findByViTri(EmployeeRole viTri);
}