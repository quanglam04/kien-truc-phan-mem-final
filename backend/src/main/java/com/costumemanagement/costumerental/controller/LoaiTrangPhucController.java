package com.costumemanagement.costumerental.controller;

import com.costumemanagement.costumerental.entity.LoaiTrangPhuc;
import com.costumemanagement.costumerental.service.LoaiTrangPhucService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loai-trang-phuc")
public class LoaiTrangPhucController extends BaseController {

    private final LoaiTrangPhucService loaiService;

    public LoaiTrangPhucController(LoaiTrangPhucService loaiService) {
        this.loaiService = loaiService;
    }

    @GetMapping
    public ResponseEntity<List<LoaiTrangPhuc>> getAll() {
        return ResponseEntity.ok(loaiService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoaiTrangPhuc> getById(@PathVariable Long id) {
        LoaiTrangPhuc loai = loaiService.findById(id);
        return loai != null ? ResponseEntity.ok(loai) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<LoaiTrangPhuc> create(@Valid @RequestBody LoaiTrangPhuc loai) {
        return ResponseEntity.ok(loaiService.save(loai));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoaiTrangPhuc> update(@PathVariable Long id, @Valid @RequestBody LoaiTrangPhuc loai) {
        LoaiTrangPhuc updated = loaiService.update(id, loai);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = loaiService.delete(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}