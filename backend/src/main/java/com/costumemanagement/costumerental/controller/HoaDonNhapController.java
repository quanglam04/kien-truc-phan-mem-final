package com.costumemanagement.costumerental.controller;

import com.costumemanagement.costumerental.entity.HoaDonNhap;
import com.costumemanagement.costumerental.service.HoaDonNhapService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/imports")
public class HoaDonNhapController extends BaseController {

    private final HoaDonNhapService importService;

    public HoaDonNhapController(HoaDonNhapService importService) {
        this.importService = importService;
    }

    @GetMapping
    public ResponseEntity<List<HoaDonNhap>> getImports() {
        logger.info("GET /api/imports - Lay danh sach hoa don nhap");
        List<HoaDonNhap> result = importService.findAll();
        logger.info("Tra ve {} hoa don nhap", result.size());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HoaDonNhap> getImportById(@PathVariable String id) {
        logger.info("GET /api/imports/{} - Lay chi tiet hoa don nhap", id);
        HoaDonNhap hoaDon = importService.findById(id);
        if (hoaDon == null) {
            logger.warn("Khong tim thay hoa don nhap: {}", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(hoaDon);
    }

    @PostMapping
    public ResponseEntity<?> createImport(@Valid @RequestBody HoaDonNhap hoaDonNhap) {
        logger.info("POST /api/imports - Tao hoa don nhap - NCC: {}", 
            hoaDonNhap.getNhaCungCap() != null ? hoaDonNhap.getNhaCungCap().getMaNCC() : "null");

        HoaDonNhap saved = importService.createImport(hoaDonNhap);
        
        if (saved == null) {
            logger.error("Tao hoa don nhap that bai");
            return ResponseEntity.badRequest().body(Map.of("message", "NCC hoặc trang phục không tồn tại"));
        }

        logger.info("Da tao hoa don: {} - Tong tien: {}", 
            saved.getMaHDNhap(), saved.getTongTien());
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<?> payImport(@PathVariable String id, 
                                       @RequestBody Map<String, String> paymentData) {
        
        String phuongThuc = paymentData.get("phuongThuc");
        logger.info("PUT /api/imports/{}/pay - Thanh toan - Phuong thuc: {}", id, phuongThuc);

        try {
            HoaDonNhap hoaDon = importService.payImport(id, phuongThuc);
            if (hoaDon == null) {
                logger.error("Khong tim thay hoa don: {}", id);
                return ResponseEntity.notFound().build();
            }
            logger.info("Hoan tat thanh toan hoa don: {} - Trang thai: {}", 
                id, hoaDon.getTrangThaiThanhToan());
            return ResponseEntity.ok(hoaDon);
        } catch (IllegalArgumentException e) {
            logger.error("Phuong thuc thanh toan khong hop le: {}", phuongThuc);
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}