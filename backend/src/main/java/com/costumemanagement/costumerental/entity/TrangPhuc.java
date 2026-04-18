package com.costumemanagement.costumerental.entity;

import com.costumemanagement.costumerental.constant.CostumeStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "trang_phuc")
public class TrangPhuc {

    @Id
    @Column(name = "ma_trang_phuc")
    private String maTrangPhuc;

    @NotBlank(message = "Tên trang phục không được để trống")
    @Column(name = "ten_trang_phuc")
    private String tenTrangPhuc;

    @ManyToOne
    @JoinColumn(name = "loai_trang_phuc_id")
    private LoaiTrangPhuc loaiTrangPhuc;

    @OneToMany(mappedBy = "trangPhuc", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KichThuocTrangPhuc> kichThuocs;

    @Column(name = "mau_sac")
    private String mauSac;

    @Min(value = 0, message = "Giá thuê phải >= 0")
    @Column(name = "gia_thue")
    private float giaThue;

    @Min(value = 0, message = "Giá gốc phải >= 0")
    @Column(name = "gia_goc")
    private float giaGoc;

    @Enumerated(EnumType.STRING)
    @Column(name = "trang_thai")
    private CostumeStatus trangThai;

    public TrangPhuc() {}


    public String getMaTrangPhuc() { return maTrangPhuc; }
    public void setMaTrangPhuc(String maTrangPhuc) { this.maTrangPhuc = maTrangPhuc; }

    public String getTenTrangPhuc() { return tenTrangPhuc; }
    public void setTenTrangPhuc(String tenTrangPhuc) { this.tenTrangPhuc = tenTrangPhuc; }

    public LoaiTrangPhuc getLoaiTrangPhuc() { return loaiTrangPhuc; }
    public void setLoaiTrangPhuc(LoaiTrangPhuc loaiTrangPhuc) { this.loaiTrangPhuc = loaiTrangPhuc; }

    public List<KichThuocTrangPhuc> getKichThuocs() { return kichThuocs; }
    public void setKichThuocs(List<KichThuocTrangPhuc> kichThuocs) { this.kichThuocs = kichThuocs; }

    public String getMauSac() { return mauSac; }
    public void setMauSac(String mauSac) { this.mauSac = mauSac; }

    public float getGiaThue() { return giaThue; }
    public void setGiaThue(float giaThue) { this.giaThue = giaThue; }

    public float getGiaGoc() { return giaGoc; }
    public void setGiaGoc(float giaGoc) { this.giaGoc = giaGoc; }

    public CostumeStatus getTrangThai() { return trangThai; }
    public void setTrangThai(CostumeStatus trangThai) { this.trangThai = trangThai; }

    /**
     * Tính tổng số lượng của tất cả các size
     */
    public int getTongSoLuong() {
        if (kichThuocs == null) return 0;
        return kichThuocs.stream().mapToInt(KichThuocTrangPhuc::getSoLuong).sum();
    }
}