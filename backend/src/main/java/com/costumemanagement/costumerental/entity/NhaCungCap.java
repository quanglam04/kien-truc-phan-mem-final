package com.costumemanagement.costumerental.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "nha_cung_cap")
public class NhaCungCap {

    @Id
    @Column(name = "ma_ncc")
    private String maNCC;

    @NotBlank(message = "Ten nha cung cap khong duoc de trong")
    @Column(name = "ten_ncc")
    private String tenNCC;

    @Column(name = "email")
    private String email;

    @NotBlank(message = "So dien thoai khong duoc de trong")
    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @ManyToMany
    @JoinTable(
            name = "trang_phuc_nha_cung_cap",
            joinColumns = @JoinColumn(name = "ma_ncc"),
            inverseJoinColumns = @JoinColumn(name = "ma_trang_phuc")
    )
    @JsonIgnore
    private List<TrangPhuc> trangPhucs;

    public NhaCungCap() {}

    // Getters and Setters
    public String getMaNCC() { return maNCC; }
    public void setMaNCC(String maNCC) { this.maNCC = maNCC; }

    public String getTenNCC() { return tenNCC; }
    public void setTenNCC(String tenNCC) { this.tenNCC = tenNCC; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public List<TrangPhuc> getTrangPhucs() { return trangPhucs; }
    public void setTrangPhucs(List<TrangPhuc> trangPhucs) { this.trangPhucs = trangPhucs; }
}
