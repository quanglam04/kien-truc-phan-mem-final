package com.costumemanagement.costumerental.repository;

import com.costumemanagement.costumerental.entity.NhaCungCap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NhaCungCapRepository extends JpaRepository<NhaCungCap, String> {

    List<NhaCungCap> findByTenNCCContaining(String keyword);
}
