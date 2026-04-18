package com.costumemanagement.costumerental.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "kich_thuoc_loai_trang_phuc")
public class KichThuocTrangPhuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ma_trang_phuc", nullable = false)
    private TrangPhuc trangPhuc;

    @ManyToOne
    @JoinColumn(name = "id_kich_thuoc", nullable = false)
    private KichThuoc kichThuoc;

    @Min(value = 0, message = "Số lượng phải >= 0")
    @Column(name = "so_luong", nullable = false)
    private int soLuong = 0;

    public KichThuocTrangPhuc() {}

    public KichThuocTrangPhuc(TrangPhuc trangPhuc, KichThuoc kichThuoc, int soLuong) {
        this.trangPhuc = trangPhuc;
        this.kichThuoc = kichThuoc;
        this.soLuong = soLuong;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TrangPhuc getTrangPhuc() { return trangPhuc; }
    public void setTrangPhuc(TrangPhuc trangPhuc) { this.trangPhuc = trangPhuc; }

    public KichThuoc getKichThuoc() { return kichThuoc; }
    public void setKichThuoc(KichThuoc kichThuoc) { this.kichThuoc = kichThuoc; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    @JsonProperty("kichThuocId")
    public Long getKichThuocId() {
        return kichThuoc != null ? kichThuoc.getId() : null;
    }

    @JsonProperty("tenKichThuoc")
    public String getTenKichThuoc() {
        return kichThuoc != null ? kichThuoc.getKichThuoc() : null;
    }
}