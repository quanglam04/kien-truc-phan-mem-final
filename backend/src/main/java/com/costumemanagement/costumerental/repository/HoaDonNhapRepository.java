package com.costumemanagement.costumerental.repository;

import com.costumemanagement.costumerental.entity.HoaDonNhap;
import com.costumemanagement.costumerental.entity.NhaCungCap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonNhapRepository extends JpaRepository<HoaDonNhap, String> {

    List<HoaDonNhap> findByNhaCungCap(NhaCungCap nhaCungCap);
}
