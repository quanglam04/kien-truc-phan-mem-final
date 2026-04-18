package com.costumemanagement.costumerental.controller;

import com.costumemanagement.costumerental.entity.NhaCungCap;
import com.costumemanagement.costumerental.service.NhaCungCapService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/suppliers")
public class NhaCungCapController extends BaseController {

    private final NhaCungCapService supplierService;

    public NhaCungCapController(NhaCungCapService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public ResponseEntity<List<NhaCungCap>> getAllSuppliers() {
        logger.info("GET /api/suppliers - Lay danh sach NCC");
        List<NhaCungCap> result = supplierService.findAll();
        logger.info("Tra ve {} NCC", result.size());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NhaCungCap> getSupplierById(@PathVariable String id) {
        logger.info("GET /api/suppliers/{} - Lay chi tiet NCC", id);
        NhaCungCap ncc = supplierService.findById(id);
        if (ncc == null) {
            logger.warn("Khong tim thay NCC: {}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ncc);
    }

    @PostMapping
    public ResponseEntity<NhaCungCap> createSupplier(@Valid @RequestBody NhaCungCap ncc) {
        logger.info("POST /api/suppliers - Tao NCC: {}", ncc.getTenNCC());
        
        // Đảm bảo mã sẽ được generate tự động
        ncc.setMaNCC(null);
        
        NhaCungCap saved = supplierService.save(ncc);
        logger.info("Da tao NCC: {} - {}", saved.getMaNCC(), saved.getTenNCC());
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NhaCungCap> updateSupplier(@PathVariable String id, 
                                                      @Valid @RequestBody NhaCungCap ncc) {
        logger.info("PUT /api/suppliers/{} - Cap nhat NCC", id);
        NhaCungCap updated = supplierService.update(id, ncc);
        if (updated == null) {
            logger.warn("Khong tim thay NCC de cap nhat: {}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable String id) {
        logger.info("DELETE /api/suppliers/{} - Xoa NCC", id);
        boolean deleted = supplierService.delete(id);
        if (!deleted) {
            logger.warn("Khong tim thay NCC de xoa: {}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<List<Map<String, Object>>> getStats() {
        logger.info("GET /api/suppliers/stats - Thong ke tat ca NCC");
        List<Map<String, Object>> stats = supplierService.getStats();
        logger.info("Tra ve thong ke {} NCC", stats.size());
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/{id}")
    public ResponseEntity<Map<String, Object>> getStatsById(@PathVariable String id) {
        logger.info("GET /api/suppliers/stats/{} - Thong ke NCC", id);
        Map<String, Object> stats = supplierService.getStatsById(id);
        if (stats == null) {
            logger.warn("Khong tim thay NCC: {}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stats);
    }
}