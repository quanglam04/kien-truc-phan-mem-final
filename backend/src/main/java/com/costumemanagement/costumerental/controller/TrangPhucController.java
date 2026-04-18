package com.costumemanagement.costumerental.controller;

import com.costumemanagement.costumerental.entity.TrangPhuc;
import com.costumemanagement.costumerental.entity.KichThuocTrangPhuc;
import com.costumemanagement.costumerental.service.TrangPhucService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/costumes")
public class TrangPhucController extends BaseController {

    private final TrangPhucService costumeService;

    public TrangPhucController(TrangPhucService costumeService) {
        this.costumeService = costumeService;
    }

    @GetMapping
    public ResponseEntity<List<TrangPhuc>> getAllCostumes() {
        logger.info("GET /api/costumes - Lay danh sach trang phuc");
        List<TrangPhuc> result = costumeService.findAll();
        logger.info("Tra ve {} trang phuc", result.size());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrangPhuc> getCostumeById(@PathVariable String id) {
        logger.info("GET /api/costumes/{} - Lay chi tiet trang phuc", id);
        TrangPhuc tp = costumeService.findById(id);
        return tp != null ? ResponseEntity.ok(tp) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<TrangPhuc> createCostume(@Valid @RequestBody TrangPhuc trangPhuc) {
        logger.info("POST /api/costumes - Tao trang phuc: {}", trangPhuc.getTenTrangPhuc());
        trangPhuc.setMaTrangPhuc(null);
        TrangPhuc saved = costumeService.save(trangPhuc);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrangPhuc> updateCostume(@PathVariable String id, @Valid @RequestBody TrangPhuc trangPhuc) {
        logger.info("PUT /api/costumes/{} - Cap nhat trang phuc", id);
        TrangPhuc updated = costumeService.update(id, trangPhuc);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCostume(@PathVariable String id) {
        logger.info("DELETE /api/costumes/{} - Xoa trang phuc", id);
        boolean deleted = costumeService.delete(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    /**
     * Lấy số lượng theo từng size của một trang phục
     * GET /api/costumes/{id}/sizes
     */
    @GetMapping("/{id}/sizes")
    public ResponseEntity<List<KichThuocTrangPhuc>> getSizeQuantities(@PathVariable String id) {
        logger.info("GET /api/costumes/{}/sizes - Lay so luong theo size", id);
        List<KichThuocTrangPhuc> result = costumeService.getSizeQuantities(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Cập nhật số lượng cho một size cụ thể
     * PUT /api/costumes/{id}/sizes/{sizeId}/quantity?delta=5
     */
    @PutMapping("/{id}/sizes/{sizeId}/quantity")
    public ResponseEntity<KichThuocTrangPhuc> updateSizeQuantity(
            @PathVariable String id,
            @PathVariable Long sizeId,
            @RequestParam int delta) {
        logger.info("PUT /api/costumes/{}/sizes/{}/quantity - delta={}", id, sizeId, delta);
        KichThuocTrangPhuc result = costumeService.updateQuantityBySize(id, sizeId, delta);
        return result != null ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
    }
}