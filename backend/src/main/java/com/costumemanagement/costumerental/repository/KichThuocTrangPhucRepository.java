package com.costumemanagement.costumerental.repository;

import com.costumemanagement.costumerental.entity.TrangPhuc;
import com.costumemanagement.costumerental.entity.KichThuocTrangPhuc;
import com.costumemanagement.costumerental.entity.KichThuoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KichThuocTrangPhucRepository extends JpaRepository<KichThuocTrangPhuc, Long> {

    List<KichThuocTrangPhuc> findByTrangPhuc(TrangPhuc trangPhuc);

    Optional<KichThuocTrangPhuc> findByTrangPhucAndKichThuoc(TrangPhuc trangPhuc, KichThuoc kichThuoc);
}
