package com.costumemanagement.costumerental.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "kich_thuoc")
public class KichThuoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "kich_thuoc", unique = true, length = 20)
    private String kichThuoc;

    @Column(name = "mo_ta", length = 255)
    private String moTa;

    public KichThuoc() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getKichThuoc() { return kichThuoc; }
    public void setKichThuoc(String kichThuoc) { this.kichThuoc = kichThuoc; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
}