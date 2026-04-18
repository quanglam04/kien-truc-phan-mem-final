import { useState, useEffect } from "react";
import { costumeApi, loaiTrangPhucApi, kichThuocApi } from "../api";
import { toast } from "../components/ThongBaoNhanh";
import Modal from "../components/HopThoai";

export default function TrangPhucPage() {
  const [costumes, setCostumes] = useState([]);
  const [loais, setLoais] = useState([]);
  const [kichThuocs, setKichThuocs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState("");
  const [filterLoai, setFilterLoai] = useState("");

  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState(null);
  // kichThuocSizes: [{ sizeId, soLuong }]
  const [form, setForm] = useState({
    tenTrangPhuc: "",
    loaiTrangPhucId: "",
    kichThuocSizes: [],
    mauSac: "",
    giaThue: "",
    giaGoc: "",
  });

  const [showAddLoaiModal, setShowAddLoaiModal] = useState(false);
  const [newLoai, setNewLoai] = useState({ loaiTrangPhuc: "", moTa: "" });
  const [formLoading, setFormLoading] = useState(false);

  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [deleteTarget, setDeleteTarget] = useState(null);
  const [showDetail, setShowDetail] = useState(false);
  const [detailItem, setDetailItem] = useState(null);

  const fetchAll = async () => {
    setLoading(true);
    try {
      const [costRes, loaiRes, kichRes] = await Promise.all([
        costumeApi.getAll(),
        loaiTrangPhucApi.getAll(),
        kichThuocApi.getAll(),
      ]);
      setCostumes(Array.isArray(costRes.data) ? costRes.data : []);
      setLoais(Array.isArray(loaiRes.data) ? loaiRes.data : []);
      setKichThuocs(Array.isArray(kichRes.data) ? kichRes.data : []);
    } catch {
      toast("Không thể tải dữ liệu", "error");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAll();
  }, []);

  const filtered = costumes.filter(
    (c) =>
      (c.tenTrangPhuc?.toLowerCase().includes(search.toLowerCase()) ||
        c.maTrangPhuc?.toLowerCase().includes(search.toLowerCase())) &&
      (!filterLoai || c.loaiTrangPhuc?.loaiTrangPhuc === filterLoai),
  );

  // Tính tổng số lượng từ kichThuocs (List<CostumeSize>)
  const getTongSoLuong = (costume) => {
    if (!costume.kichThuocs || costume.kichThuocs.length === 0) return 0;
    return costume.kichThuocs.reduce((sum, ks) => sum + (ks.soLuong || 0), 0);
  };

  // Lấy tên size an toàn từ CostumeSize — dùng flat field tenKichThuoc nếu có
  const getSizeName = (ks) => {
    if (!ks) return "—";
    // Dùng flat field từ @JsonProperty
    if (ks.tenKichThuoc) return ks.tenKichThuoc;
    // Fallback: nested object
    if (ks.kichThuoc && typeof ks.kichThuoc === "object")
      return ks.kichThuoc.kichThuoc || "—";
    if (typeof ks.kichThuoc === "string") return ks.kichThuoc;
    return "—";
  };

  // Lấy id size an toàn từ CostumeSize
  const getSizeId = (ks) => {
    if (!ks) return null;
    if (ks.kichThuocId) return ks.kichThuocId;
    if (ks.kichThuoc && typeof ks.kichThuoc === "object")
      return ks.kichThuoc.id;
    return null;
  };

  const toggleSize = (sizeId) => {
    const exists = form.kichThuocSizes.find((ks) => ks.sizeId === sizeId);
    if (exists) {
      setForm({
        ...form,
        kichThuocSizes: form.kichThuocSizes.filter(
          (ks) => ks.sizeId !== sizeId,
        ),
      });
    } else {
      setForm({
        ...form,
        kichThuocSizes: [...form.kichThuocSizes, { sizeId, soLuong: 0 }],
      });
    }
  };

  const updateSizeQty = (sizeId, soLuong) => {
    setForm({
      ...form,
      kichThuocSizes: form.kichThuocSizes.map((ks) =>
        ks.sizeId === sizeId ? { ...ks, soLuong: parseInt(soLuong) || 0 } : ks,
      ),
    });
  };

  const openCreate = () => {
    setForm({
      tenTrangPhuc: "",
      loaiTrangPhucId: loais.length > 0 ? loais[0].id.toString() : "",
      kichThuocSizes: [],
      mauSac: "",
      giaThue: "",
      giaGoc: "",
    });
    setEditingId(null);
    setShowForm(true);
  };

  const openEdit = (item) => {
    const kichThuocSizes = (item.kichThuocs || [])
      .map((ks) => ({
        sizeId: getSizeId(ks),
        soLuong: ks.soLuong || 0,
      }))
      .filter((ks) => ks.sizeId != null);

    setForm({
      tenTrangPhuc: item.tenTrangPhuc,
      loaiTrangPhucId: item.loaiTrangPhuc?.id?.toString() || "",
      kichThuocSizes,
      mauSac: item.mauSac || "",
      giaThue: item.giaThue,
      giaGoc: item.giaGoc,
    });
    setEditingId(item.maTrangPhuc);
    setShowForm(true);
  };

  const handleAddNewLoai = async () => {
    if (!newLoai.loaiTrangPhuc.trim())
      return toast("Vui lòng nhập tên loại", "error");
    try {
      await loaiTrangPhucApi.create(newLoai);
      toast("Thêm loại thành công");
      setShowAddLoaiModal(false);
      setNewLoai({ loaiTrangPhuc: "", moTa: "" });
      fetchAll();
    } catch {
      toast("Thêm loại thất bại", "error");
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.loaiTrangPhucId)
      return toast("Vui lòng chọn loại trang phục", "error");
    if (form.kichThuocSizes.length === 0)
      return toast("Vui lòng chọn ít nhất một kích thước", "error");

    setFormLoading(true);
    const payload = {
      tenTrangPhuc: form.tenTrangPhuc,
      loaiTrangPhuc: { id: parseInt(form.loaiTrangPhucId) },
      kichThuocs: form.kichThuocSizes.map((ks) => ({
        kichThuoc: { id: ks.sizeId },
        soLuong: ks.soLuong,
      })),
      mauSac: form.mauSac,
      giaThue: parseFloat(form.giaThue),
      giaGoc: parseFloat(form.giaGoc || 0),
    };

    try {
      if (editingId) {
        await costumeApi.update(editingId, payload);
        toast("Cập nhật thành công");
      } else {
        await costumeApi.create(payload);
        toast("Thêm mới thành công");
      }
      setShowForm(false);
      fetchAll();
    } catch (err) {
      toast(err?.response?.data?.message || "Có lỗi xảy ra", "error");
    } finally {
      setFormLoading(false);
    }
  };

  const confirmDelete = (item) => {
    setDeleteTarget(item);
    setShowDeleteModal(true);
  };

  const handleDelete = async () => {
    try {
      await costumeApi.delete(deleteTarget.maTrangPhuc);
      toast("Đã xóa trang phục thành công");
      setShowDeleteModal(false);
      fetchAll();
    } catch {
      toast("Xóa thất bại", "error");
    }
  };

  const openDetail = (item) => {
    setDetailItem(item);
    setShowDetail(true);
  };

  const fmtPrice = (n) =>
    new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
    }).format(n);

  const TRANG_THAI_BADGE = {
    CON_HANG: { label: "Còn hàng", cls: "badge-green" },
    HET_HANG: { label: "Hết hàng", cls: "badge-red" },
  };

  return (
    <div className="page">
      <div className="page-header">
        <div>
          <h1 className="page-title">Quản lý trang phục</h1>
          <p className="page-subtitle">Danh sách trang phục cho thuê</p>
        </div>
        <button className="btn btn-primary" onClick={openCreate}>
          <span>＋</span> Thêm trang phục
        </button>
      </div>

      <div className="toolbar">
        <div className="search-box">
          <span className="search-icon">⌕</span>
          <input
            type="text"
            placeholder="Tìm theo tên hoặc mã..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>
        <select
          className="filter-select"
          value={filterLoai}
          onChange={(e) => setFilterLoai(e.target.value)}
        >
          <option value="">Tất cả loại</option>
          {loais.map((l) => (
            <option key={l.id} value={l.loaiTrangPhuc}>
              {l.loaiTrangPhuc}
            </option>
          ))}
        </select>
        <span className="count-badge">{filtered.length} trang phục</span>
      </div>

      {loading ? (
        <div className="loading-state">
          <div className="spinner" />
          <p>Đang tải...</p>
        </div>
      ) : (
        <div className="table-wrap">
          <table className="data-table">
            <thead>
              <tr>
                <th>Mã TP</th>
                <th>Tên trang phục</th>
                <th>Loại</th>
                <th>Kích thước & Số lượng</th>
                <th>Màu sắc</th>
                <th>Giá thuê</th>
                <th>Tổng SL</th>
                <th>Trạng thái</th>
                <th>Thao tác</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map((c) => {
                const badge = TRANG_THAI_BADGE[c.trangThai] || {
                  label: c.trangThai,
                  cls: "badge-gray",
                };
                const tongSL = getTongSoLuong(c);
                return (
                  <tr key={c.maTrangPhuc}>
                    <td>
                      <code className="code-cell">{c.maTrangPhuc}</code>
                    </td>
                    <td className="name-cell">
                      <button
                        className="link-btn"
                        onClick={() => openDetail(c)}
                      >
                        {c.tenTrangPhuc}
                      </button>
                    </td>
                    <td>{c.loaiTrangPhuc?.loaiTrangPhuc}</td>
                    <td>
                      <div className="size-qty-list">
                        {c.kichThuocs && c.kichThuocs.length > 0 ? (
                          c.kichThuocs.map((ks) => (
                            <span
                              key={ks.id}
                              className={`size-qty-tag ${ks.soLuong === 0 ? "size-qty-zero" : ""}`}
                              title={`${getSizeName(ks)}: ${ks.soLuong} cái`}
                            >
                              {getSizeName(ks)}
                              <em>{ks.soLuong}</em>
                            </span>
                          ))
                        ) : (
                          <span className="text-muted">—</span>
                        )}
                      </div>
                    </td>
                    <td>
                      <div className="color-cell">
                        <span
                          className="color-dot"
                          style={{ backgroundColor: c.mauSac }}
                        />
                        {c.mauSac}
                      </div>
                    </td>
                    <td className="price-cell">{fmtPrice(c.giaThue)}</td>
                    <td>
                      <span
                        className={`qty-badge ${tongSL === 0 ? "qty-zero" : ""}`}
                      >
                        {tongSL}
                      </span>
                    </td>
                    <td>
                      <span className={`badge ${badge.cls}`}>
                        {badge.label}
                      </span>
                    </td>
                    <td>
                      <div className="action-btns">
                        <button
                          className="btn-icon btn-edit"
                          onClick={() => openEdit(c)}
                          title="Sửa"
                        >
                          ✎
                        </button>
                        <button
                          className="btn-icon btn-delete"
                          onClick={() => confirmDelete(c)}
                          title="Xóa"
                        >
                          🗑
                        </button>
                      </div>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      )}

      {/* Form Thêm/Sửa */}
      <Modal
        isOpen={showForm}
        onClose={() => setShowForm(false)}
        title={editingId ? "Sửa trang phục" : "Thêm trang phục mới"}
      >
        <form onSubmit={handleSubmit} className="form-grid">
          <div className="form-row">
            <label>Tên trang phục *</label>
            <input
              required
              value={form.tenTrangPhuc}
              onChange={(e) =>
                setForm({ ...form, tenTrangPhuc: e.target.value })
              }
            />
          </div>

          <div className="form-row">
            <label>Loại trang phục *</label>
            <div style={{ display: "flex", gap: "8px" }}>
              <select
                required
                value={form.loaiTrangPhucId}
                onChange={(e) =>
                  setForm({ ...form, loaiTrangPhucId: e.target.value })
                }
                style={{ flex: 1 }}
              >
                <option value="">-- Chọn loại --</option>
                {loais.map((l) => (
                  <option key={l.id} value={l.id}>
                    {l.loaiTrangPhuc}
                  </option>
                ))}
              </select>
              <button
                type="button"
                className="btn btn-ghost"
                onClick={() => setShowAddLoaiModal(true)}
              >
                ＋ Thêm loại
              </button>
            </div>
          </div>

          <div className="form-row">
            <label>Kích thước & Số lượng *</label>
            <div className="size-qty-editor">
              {kichThuocs.map((k) => {
                // k từ /api/kich-thuoc: { id, kichThuoc: "S", moTa }
                const sizeName =
                  typeof k.kichThuoc === "string"
                    ? k.kichThuoc
                    : k.kichThuoc?.kichThuoc || String(k.id);
                const selected = form.kichThuocSizes.find(
                  (ks) => ks.sizeId === k.id,
                );
                return (
                  <div
                    key={k.id}
                    className={`size-qty-row ${selected ? "size-qty-row-active" : ""}`}
                  >
                    <button
                      type="button"
                      className={`size-chip ${selected ? "size-chip-active" : ""}`}
                      onClick={() => toggleSize(k.id)}
                    >
                      {selected && <span className="size-chip-check">✓</span>}
                      {sizeName}
                    </button>
                    {selected && (
                      <input
                        type="number"
                        min="0"
                        value={selected.soLuong}
                        onChange={(e) => updateSizeQty(k.id, e.target.value)}
                        placeholder="SL"
                        className="size-qty-input"
                      />
                    )}
                  </div>
                );
              })}
            </div>
            {form.kichThuocSizes.length > 0 && (
              <p className="size-qty-hint">
                Tổng:{" "}
                {form.kichThuocSizes.reduce(
                  (s, ks) => s + (ks.soLuong || 0),
                  0,
                )}{" "}
                cái
              </p>
            )}
          </div>

          <div className="form-row">
            <label>Màu sắc</label>
            <input
              value={form.mauSac}
              onChange={(e) => setForm({ ...form, mauSac: e.target.value })}
            />
          </div>

          <div className="form-row-2col">
            <div className="form-row">
              <label>Giá thuê (VNĐ) *</label>
              <input
                type="number"
                required
                min="0"
                value={form.giaThue}
                onChange={(e) => setForm({ ...form, giaThue: e.target.value })}
              />
            </div>
            <div className="form-row">
              <label>Giá gốc (VNĐ)</label>
              <input
                type="number"
                min="0"
                value={form.giaGoc}
                onChange={(e) => setForm({ ...form, giaGoc: e.target.value })}
              />
            </div>
          </div>

          <div className="form-actions">
            <button
              type="button"
              className="btn btn-ghost"
              onClick={() => setShowForm(false)}
            >
              Hủy
            </button>
            <button
              type="submit"
              className="btn btn-primary"
              disabled={formLoading}
            >
              {formLoading
                ? "Đang lưu..."
                : editingId
                  ? "Cập nhật"
                  : "Thêm mới"}
            </button>
          </div>
        </form>
      </Modal>

      {/* Modal thêm loại */}
      <Modal
        isOpen={showAddLoaiModal}
        onClose={() => setShowAddLoaiModal(false)}
        title="Thêm loại trang phục mới"
      >
        <div className="form-grid">
          <div className="form-row">
            <label>Tên loại *</label>
            <input
              value={newLoai.loaiTrangPhuc}
              onChange={(e) =>
                setNewLoai({ ...newLoai, loaiTrangPhuc: e.target.value })
              }
            />
          </div>
          <div className="form-row">
            <label>Mô tả</label>
            <textarea
              value={newLoai.moTa}
              onChange={(e) => setNewLoai({ ...newLoai, moTa: e.target.value })}
              rows={3}
            />
          </div>
          <div className="form-actions">
            <button
              className="btn btn-ghost"
              onClick={() => setShowAddLoaiModal(false)}
            >
              Hủy
            </button>
            <button className="btn btn-primary" onClick={handleAddNewLoai}>
              Thêm loại
            </button>
          </div>
        </div>
      </Modal>

      {/* Modal Xác nhận xóa */}
      <Modal
        isOpen={showDeleteModal}
        onClose={() => setShowDeleteModal(false)}
        title="Xác nhận xóa"
      >
        <div className="confirm-box">
          <p>
            Bạn có chắc muốn xóa trang phục{" "}
            <strong>{deleteTarget?.tenTrangPhuc}</strong>?
          </p>
          <div className="form-actions">
            <button
              className="btn btn-ghost"
              onClick={() => setShowDeleteModal(false)}
            >
              Hủy
            </button>
            <button className="btn btn-danger" onClick={handleDelete}>
              Xác nhận xóa
            </button>
          </div>
        </div>
      </Modal>

      {/* Modal Chi tiết */}
      <Modal
        isOpen={showDetail}
        onClose={() => setShowDetail(false)}
        title="Chi tiết trang phục"
      >
        {detailItem && (
          <div className="detail-grid">
            <div className="detail-row">
              <span className="detail-label">Mã</span>
              <span>{detailItem.maTrangPhuc}</span>
            </div>
            <div className="detail-row">
              <span className="detail-label">Tên</span>
              <span>{detailItem.tenTrangPhuc}</span>
            </div>
            <div className="detail-row">
              <span className="detail-label">Loại</span>
              <span>{detailItem.loaiTrangPhuc?.loaiTrangPhuc}</span>
            </div>
            <div className="detail-row">
              <span className="detail-label">Màu sắc</span>
              <span>{detailItem.mauSac}</span>
            </div>
            <div className="detail-row">
              <span className="detail-label">Giá thuê</span>
              <span>{fmtPrice(detailItem.giaThue)}</span>
            </div>
            <div className="detail-row">
              <span className="detail-label">Tổng số lượng</span>
              <span>{getTongSoLuong(detailItem)} cái</span>
            </div>
            {detailItem.kichThuocs && detailItem.kichThuocs.length > 0 && (
              <div className="detail-row detail-row-full">
                <span className="detail-label">Số lượng theo size</span>
                <div className="size-detail-table">
                  {detailItem.kichThuocs.map((ks) => (
                    <div key={ks.id} className="size-detail-row">
                      <span className="size-tag">{getSizeName(ks)}</span>
                      <span
                        className={`qty-badge ${ks.soLuong === 0 ? "qty-zero" : ""}`}
                      >
                        {ks.soLuong} cái
                      </span>
                    </div>
                  ))}
                </div>
              </div>
            )}
          </div>
        )}
      </Modal>
    </div>
  );
}
