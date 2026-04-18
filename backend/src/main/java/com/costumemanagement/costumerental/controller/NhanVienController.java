package com.costumemanagement.costumerental.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.costumemanagement.costumerental.constant.EmployeeRole;
import com.costumemanagement.costumerental.entity.NhanVien;
import com.costumemanagement.costumerental.repository.NhanVienRepository;

@RestController
@RequestMapping("/api/employee-warehouse")
public class NhanVienController extends BaseController {
    private final NhanVienRepository employeeWarehouseRepository;

    public NhanVienController(NhanVienRepository employeeWarehouseRepository){
        this.employeeWarehouseRepository = employeeWarehouseRepository;
    }

    @GetMapping
    public ResponseEntity<List<NhanVien>> getAllEmployeeWarehouse() {
        logger.info("GET /api/employee - Lay danh sach nhan vien ");
        List<NhanVien> result = employeeWarehouseRepository.findByViTri(EmployeeRole.NHAN_VIEN_KHO);
        logger.info("Tra ve {} nhan vien kho",result.size());
        return ResponseEntity.ok(result);
    }
}
