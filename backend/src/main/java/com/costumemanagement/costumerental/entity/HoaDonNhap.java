package com.costumemanagement.costumerental.entity;

import com.costumemanagement.costumerental.constant.PaymentMethod;
import com.costumemanagement.costumerental.constant.PaymentStatus;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "hoa_don_nhap")
public class HoaDonNhap {

    @Id
    @Column(name = "ma_hd_nhap")
    private String maHDNhap;

    @Column(name = "tong_tien")
    private float tongTien;

    @Column(name = "ngay_nhap")
    private LocalDate ngayNhap;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai_thanh_toan")
    private PaymentStatus trangThaiThanhToan;

    @Enumerated(EnumType.STRING)
    @Column(name = "phuong_thuc_thanh_toan")
    private PaymentMethod phuongThucThanhToan;

    @ManyToOne
    @JoinColumn(name = "ma_ncc")
    private NhaCungCap nhaCungCap;

    @ManyToOne
    @JoinColumn(name = "ma_nhan_vien")
    private NhanVien nhanVien;

    @OneToMany(mappedBy = "hoaDonNhap", cascade = CascadeType.ALL)
    private List<ChiTietHoaDonNhap> chiTietHDNhap;

    public HoaDonNhap() {}

    private HoaDonNhap(Builder builder) {
        this.maHDNhap = builder.maHDNhap;
        this.tongTien = builder.tongTien;
        this.ngayNhap = builder.ngayNhap;
        this.trangThaiThanhToan = builder.trangThaiThanhToan;
        this.phuongThucThanhToan = builder.phuongThucThanhToan;
        this.nhaCungCap = builder.nhaCungCap;
        this.nhanVien = builder.nhanVien;
        this.chiTietHDNhap = builder.chiTietHDNhap;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String maHDNhap;
        private float tongTien;
        private LocalDate ngayNhap;
        private PaymentStatus trangThaiThanhToan;
        private PaymentMethod phuongThucThanhToan;
        private NhaCungCap nhaCungCap;
        private NhanVien nhanVien;
        private List<ChiTietHoaDonNhap> chiTietHDNhap;

        public Builder maHDNhap(String maHDNhap) {
            this.maHDNhap = maHDNhap;
            return this;
        }

        public Builder tongTien(float tongTien) {
            this.tongTien = tongTien;
            return this;
        }

        public Builder ngayNhap(LocalDate ngayNhap) {
            this.ngayNhap = ngayNhap;
            return this;
        }

        public Builder trangThaiThanhToan(PaymentStatus trangThaiThanhToan) {
            this.trangThaiThanhToan = trangThaiThanhToan;
            return this;
        }

        public Builder phuongThucThanhToan(PaymentMethod phuongThucThanhToan) {
            this.phuongThucThanhToan = phuongThucThanhToan;
            return this;
        }

        public Builder nhaCungCap(NhaCungCap nhaCungCap) {
            this.nhaCungCap = nhaCungCap;
            return this;
        }

        public Builder nhanVien(NhanVien nhanVien) {
            this.nhanVien = nhanVien;
            return this;
        }

        public Builder chiTietHDNhap(List<ChiTietHoaDonNhap> chiTietHDNhap) {
            this.chiTietHDNhap = chiTietHDNhap;
            return this;
        }

        public HoaDonNhap build() {
            return new HoaDonNhap(this);
        }
    }

    // Getters and Setters
    public String getMaHDNhap() { return maHDNhap; }
    public void setMaHDNhap(String maHDNhap) { this.maHDNhap = maHDNhap; }

    public float getTongTien() { return tongTien; }
    public void setTongTien(float tongTien) { this.tongTien = tongTien; }

    public LocalDate getNgayNhap() { return ngayNhap; }
    public void setNgayNhap(LocalDate ngayNhap) { this.ngayNhap = ngayNhap; }

    public PaymentStatus getTrangThaiThanhToan() { return trangThaiThanhToan; }
    public void setTrangThaiThanhToan(PaymentStatus trangThaiThanhToan) { this.trangThaiThanhToan = trangThaiThanhToan; }

    public PaymentMethod getPhuongThucThanhToan() { return phuongThucThanhToan; }
    public void setPhuongThucThanhToan(PaymentMethod phuongThucThanhToan) { this.phuongThucThanhToan = phuongThucThanhToan; }

    public NhaCungCap getNhaCungCap() { return nhaCungCap; }
    public void setNhaCungCap(NhaCungCap nhaCungCap) { this.nhaCungCap = nhaCungCap; }

    public NhanVien getNhanVien() { return nhanVien; }
    public void setNhanVien(NhanVien nhanVien) { this.nhanVien = nhanVien; }

    public List<ChiTietHoaDonNhap> getChiTietHDNhap() { return chiTietHDNhap; }
    public void setChiTietHDNhap(List<ChiTietHoaDonNhap> chiTietHDNhap) { this.chiTietHDNhap = chiTietHDNhap; }
}
