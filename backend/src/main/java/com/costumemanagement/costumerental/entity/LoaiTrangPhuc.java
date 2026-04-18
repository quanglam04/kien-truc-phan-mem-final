package com.costumemanagement.costumerental.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "loai_trang_phuc")
public class LoaiTrangPhuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "loai_trang_phuc", unique = true, length = 100)
    private String loaiTrangPhuc;

    @Column(name = "mo_ta", length = 255)
    private String moTa;

    @OneToMany(mappedBy = "loaiTrangPhuc", cascade = CascadeType.ALL)
    private List<TrangPhuc> trangPhucs;

    public LoaiTrangPhuc() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLoaiTrangPhuc() { return loaiTrangPhuc; }
    public void setLoaiTrangPhuc(String loaiTrangPhuc) { this.loaiTrangPhuc = loaiTrangPhuc; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    @JsonIgnore
    public List<TrangPhuc> getTrangPhucs() { return trangPhucs; }
    public void setTrangPhucs(List<TrangPhuc> trangPhucs) { this.trangPhucs = trangPhucs; }
}