package com.costumemanagement.costumerental.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

import com.costumemanagement.costumerental.constant.EmployeeRole;

@Entity
@Table(name = "nhan_vien")
public class NhanVien {


    @Id
    @Column(name = "ma_nhan_vien", unique = true)
    private String maNhanVien;

    @Column(name = "ho_ten")
    private String hoTen;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "vi_tri")
    @Enumerated(EnumType.STRING)
    private EmployeeRole viTri;

    public NhanVien() {}

    public String getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(String maNhanVien) { this.maNhanVien = maNhanVien; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public EmployeeRole getViTri() { return viTri; }
    public void setViTri(EmployeeRole viTri) { this.viTri = viTri; }
}