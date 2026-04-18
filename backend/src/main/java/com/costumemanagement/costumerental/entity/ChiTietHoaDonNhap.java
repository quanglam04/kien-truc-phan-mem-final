package com.costumemanagement.costumerental.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "chi_tiet_hd_nhap")
public class ChiTietHoaDonNhap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "so_luong")
    private int soLuong;

    @Column(name = "don_gia_nhap")
    private float donGiaNhap;

    @Column(name = "thanh_tien")
    private float thanhTien;

    @ManyToOne
    @JoinColumn(name = "id_kich_thuoc")
    private KichThuoc kichThuoc;

    @ManyToOne
    @JoinColumn(name = "ma_trang_phuc")
    private TrangPhuc trangPhuc;

    @ManyToOne
    @JoinColumn(name = "ma_hd_nhap")
    @JsonIgnore
    private HoaDonNhap hoaDonNhap;

    public ChiTietHoaDonNhap() {}

    public ChiTietHoaDonNhap(int soLuong, float donGiaNhap, TrangPhuc trangPhuc) {
        this.soLuong = soLuong;
        this.donGiaNhap = donGiaNhap;
        this.trangPhuc = trangPhuc;
        this.thanhTien = soLuong * donGiaNhap;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public float getDonGiaNhap() { return donGiaNhap; }
    public void setDonGiaNhap(float donGiaNhap) { this.donGiaNhap = donGiaNhap; }

    public float getThanhTien() { return thanhTien; }
    public void setThanhTien(float thanhTien) { this.thanhTien = thanhTien; }

    public TrangPhuc getTrangPhuc() { return trangPhuc; }
    public void setTrangPhuc(TrangPhuc trangPhuc) { this.trangPhuc = trangPhuc; }

    public KichThuoc getKichThuoc() { return kichThuoc; }
    public void setKichThuoc(KichThuoc kichThuoc) { this.kichThuoc = kichThuoc; }

    public HoaDonNhap getHoaDonNhap() { return hoaDonNhap; }
    public void setHoaDonNhap(HoaDonNhap hoaDonNhap) { this.hoaDonNhap = hoaDonNhap; }
}
