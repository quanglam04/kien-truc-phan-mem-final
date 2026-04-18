package com.costumemanagement.costumerental.controller;

import com.costumemanagement.costumerental.entity.KichThuoc;
import com.costumemanagement.costumerental.service.KichThuocService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kich-thuoc")
public class KichThuocController extends BaseController {

    private final KichThuocService kichThuocService;

    public KichThuocController(KichThuocService kichThuocService) {
        this.kichThuocService = kichThuocService;
    }

    @GetMapping
    public ResponseEntity<List<KichThuoc>> getAll() {
        return ResponseEntity.ok(kichThuocService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<KichThuoc> getById(@PathVariable Long id) {
        KichThuoc kt = kichThuocService.findById(id);
        return kt != null ? ResponseEntity.ok(kt) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<KichThuoc> create(@Valid @RequestBody KichThuoc kichThuoc) {
        return ResponseEntity.ok(kichThuocService.save(kichThuoc));
    }

    @PutMapping("/{id}")
    public ResponseEntity<KichThuoc> update(@PathVariable Long id, @Valid @RequestBody KichThuoc kichThuoc) {
        KichThuoc updated = kichThuocService.update(id, kichThuoc);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = kichThuocService.delete(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}