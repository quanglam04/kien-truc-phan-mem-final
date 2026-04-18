import { useState, useEffect } from "react";
import { importApi, supplierApi, costumeApi, employeeApi } from "../api";
import Modal from "../components/HopThoai";
import { toast } from "../components/ThongBaoNhanh";

const STATUS_BADGE = {
  CHO_THANH_TOAN: { label: "Chờ thanh toán", cls: "badge-yellow" },
  DA_THANH_TOAN: { label: "Đã thanh toán", cls: "badge-green" },
};

export default function NhapTrangPhucPage() {
  const [imports, setImports] = useState([]);
  const [loading, setLoading] = useState(true);
  const [suppliers, setSuppliers] = useState([]);
  const [costumes, setCostumes] = useState([]);
  const [employees, setEmployees] = useState([]);

  const [showCreate, setShowCreate] = useState(false);
  const [formLoading, setFormLoading] = useState(false);

  // chiTiet: [{ trangPhucId, kichThuocId, soLuong, donGiaNhap }]
  const [form, setForm] = useState({
    maNCC: "",
    maNhanVien: "",
    chiTiet: [{ trangPhucId: "", kichThuocId: "", soLuong: 1, donGiaNhap: 0 }],
  });

  const [showDetail, setShowDetail] = useState(false);
  const [detailItem, setDetailItem] = useState(null);

  const [payLoading, setPayLoading] = useState("");
  const [showPayModal, setShowPayModal] = useState(false);
  const [payTarget, setPayTarget] = useState(null);
  const [phuongThuc, setPhuongThuc] = useState("TIEN_MAT");

  const PHUONG_THUC_OPTIONS = [
    { value: "TIEN_MAT", label: "Tiền mặt" },
    { value: "THE", label: "Thẻ" },
    { value: "CHUYEN_KHOAN", label: "Chuyển khoản" },
  ];

  // Lấy tên size an toàn từ CostumeSize
  const getSizeName = (ks) => {
    if (!ks) return "—";
    if (ks.tenKichThuoc) return ks.tenKichThuoc;
    if (ks.kichThuoc && typeof ks.kichThuoc === "object")
      return ks.kichThuoc.kichThuoc || "—";
    if (typeof ks.kichThuoc === "string") return ks.kichThuoc;
    return "—";
  };

  const getSizeId = (ks) => {
    if (!ks) return null;
    if (ks.kichThuocId) return ks.kichThuocId;
    if (ks.kichThuoc && typeof ks.kichThuoc === "object")
      return ks.kichThuoc.id;
    return null;
  };

  const fetchAll = async () => {
    setLoading(true);
    try {
      const [imp, sup, cos, emp] = await Promise.all([
        importApi.getAll(),
        supplierApi.getAll(),
        costumeApi.getAll(),
        employeeApi.getAll(),
      ]);
      setImports(Array.isArray(imp.data) ? imp.data : []);
      setSuppliers(Array.isArray(sup.data) ? sup.data : []);
      setCostumes(Array.isArray(cos.data) ? cos.data : []);
      setEmployees(Array.isArray(emp.data) ? emp.data : []);
    } catch {
      toast("Không thể tải dữ liệu", "error");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAll();
  }, []);

  const openCreate = () => {
    setForm({
      maNCC: "",
      maNhanVien: "",
      chiTiet: [
        { trangPhucId: "", kichThuocId: "", soLuong: 1, donGiaNhap: 0 },
      ],
    });
    setShowCreate(true);
  };

  const addDetail = () =>
    setForm((f) => ({
      ...f,
      chiTiet: [
        ...f.chiTiet,
        { trangPhucId: "", kichThuocId: "", soLuong: 1, donGiaNhap: 0 },
      ],
    }));

  const removeDetail = (i) =>
    setForm((f) => ({
      ...f,
      chiTiet: f.chiTiet.filter((_, idx) => idx !== i),
    }));

  const updateDetail = (i, field, value) => {
    setForm((f) => {
      const chiTiet = [...f.chiTiet];
      // Khi đổi trang phục thì reset kichThuocId
      if (field === "trangPhucId") {
        chiTiet[i] = { ...chiTiet[i], trangPhucId: value, kichThuocId: "" };
      } else {
        chiTiet[i] = { ...chiTiet[i], [field]: value };
      }
      return { ...f, chiTiet };
    });
  };

  // Lấy danh sách size của một trang phục đã chọn
  const getSizesOfCostume = (maTrangPhuc) => {
    const costume = costumes.find((c) => c.maTrangPhuc === maTrangPhuc);
    if (!costume || !costume.kichThuocs) return [];
    // kichThuocs là List<CostumeSize>: { id, kichThuoc: {id, kichThuoc}, soLuong, tenKichThuoc, kichThuocId }
    return costume.kichThuocs;
  };

  const tongTien = form.chiTiet.reduce(
    (s, d) => s + (parseInt(d.soLuong) || 0) * (parseFloat(d.donGiaNhap) || 0),
    0,
  );

  const handleCreate = async (e) => {
    e.preventDefault();
    if (form.chiTiet.length === 0)
      return toast("Phải có ít nhất 1 trang phục", "error");

    // Kiểm tra tất cả dòng đã chọn trang phục và size
    for (const d of form.chiTiet) {
      if (!d.trangPhucId)
        return toast("Vui lòng chọn trang phục cho tất cả dòng", "error");
      if (!d.kichThuocId)
        return toast("Vui lòng chọn kích thước cho tất cả dòng", "error");
    }

    setFormLoading(true);
    try {
      const payload = {
        nhaCungCap: { maNCC: form.maNCC },
        nhanVien: form.maNhanVien ? { maNhanVien: form.maNhanVien } : null,
        chiTietHDNhap: form.chiTiet.map((d) => ({
          trangPhuc: { maTrangPhuc: d.trangPhucId },
          // Truyền kichThuoc để backend cập nhật đúng size
          kichThuoc: { id: parseInt(d.kichThuocId) },
          soLuong: parseInt(d.soLuong),
          donGiaNhap: parseFloat(d.donGiaNhap),
        })),
      };

      await importApi.create(payload);
      toast("Tạo hóa đơn nhập thành công");
      setShowCreate(false);
      await fetchAll();
    } catch (err) {
      toast(err?.response?.data?.message || "Tạo hóa đơn thất bại", "error");
    } finally {
      setFormLoading(false);
    }
  };

  const openPayModal = (imp) => {
    setPayTarget(imp);
    setPhuongThuc("TIEN_MAT");
    setShowPayModal(true);
  };

  const handlePay = async () => {
    setPayLoading(payTarget.maHDNhap);
    try {
      await importApi.pay(payTarget.maHDNhap, { phuongThuc });
      toast("Thanh toán thành công!");
      setShowPayModal(false);
      fetchAll();
    } catch {
      toast("Thanh toán thất bại", "error");
    } finally {
      setPayLoading("");
    }
  };

  const openDetail = async (item) => {
    try {
      const res = await importApi.getById(item.maHDNhap);
      setDetailItem(res.data);
      setShowDetail(true);
    } catch {
      toast("Không thể tải chi tiết", "error");
    }
  };

  const fmtPrice = (n) =>
    new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND",
    }).format(n);

  const fmtDate = (d) =>
    d
      ? new Date(d).toLocaleDateString("vi-VN", {
          day: "2-digit",
          month: "2-digit",
          year: "numeric",
        })
      : "—";

  return (
    <div className="page">
      <div className="page-header">
        <div>
          <h1 className="page-title">Nhập trang phục từ nhà cung cấp</h1>
          <p className="page-subtitle">Nhập trang phục từ nhà cung cấp</p>
        </div>
        <button className="btn btn-primary" onClick={openCreate}>
          <span>＋</span> Tạo hóa đơn
        </button>
      </div>

      {loading ? (
        <div className="loading-state">
          <div className="spinner" />
          <p>Đang tải...</p>
        </div>
      ) : imports.length === 0 ? (
        <div className="empty-state">
          <div className="empty-icon">📦</div>
          <p>Chưa có hóa đơn nhập nào</p>
          <button className="btn btn-primary" onClick={openCreate}>
            Tạo ngay
          </button>
        </div>
      ) : (
        <div className="table-wrap">
          <table className="data-table">
            <thead>
              <tr>
                <th>Mã HD</th>
                <th>Nhà cung cấp</th>
                <th>Nhân viên phụ trách</th>
                <th>Ngày nhập</th>
                <th>Tổng tiền</th>
                <th>Trạng thái</th>
                <th>Thao tác</th>
              </tr>
            </thead>
            <tbody>
              {imports.map((imp) => {
                const badge = STATUS_BADGE[imp.trangThaiThanhToan] || {
                  label: imp.trangThaiThanhToan,
                  cls: "badge-gray",
                };
                return (
                  <tr key={imp.maHDNhap}>
                    <td>
                      <code className="code-cell">{imp.maHDNhap}</code>
                    </td>
                    <td className="fw-medium">
                      {imp.nhaCungCap?.tenNCC || imp.nhaCungCap?.maNCC || "—"}
                    </td>
                    <td>{imp.nhanVien?.hoTen || "—"}</td>
                    <td className="date-cell">{fmtDate(imp.ngayNhap)}</td>
                    <td className="price-cell">{fmtPrice(imp.tongTien)}</td>
                    <td>
                      <span className={`badge ${badge.cls}`}>
                        {badge.label}
                      </span>
                    </td>
                    <td>
                      <div className="action-btns">
                        <button
                          className="btn-icon btn-view"
                          onClick={() => openDetail(imp)}
                          title="Xem chi tiết"
                        >
                          👁
                        </button>
                        {imp.trangThaiThanhToan === "CHO_THANH_TOAN" && (
                          <button
                            className="btn btn-pay btn-sm"
                            onClick={() => openPayModal(imp)}
                            disabled={payLoading === imp.maHDNhap}
                          >
                            {payLoading === imp.maHDNhap ? "..." : "Thanh toán"}
                          </button>
                        )}
                      </div>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      )}

      {/* Create Import Modal */}
      <Modal
        isOpen={showCreate}
        wide
        onClose={() => setShowCreate(false)}
        title="Tạo hóa đơn nhập mới"
      >
        <form onSubmit={handleCreate} className="form-grid">
          <div className="form-row-2col">
            <div className="form-row">
              <label>Nhà cung cấp *</label>
              <select
                required
                value={form.maNCC}
                onChange={(e) => setForm({ ...form, maNCC: e.target.value })}
              >
                <option value="">-- Chọn NCC --</option>
                {suppliers.map((s) => (
                  <option key={s.maNCC} value={s.maNCC}>
                    {s.tenNCC} ({s.maNCC})
                  </option>
                ))}
              </select>
            </div>
            <div className="form-row">
              <label>Nhân viên phụ trách</label>
              <select
                value={form.maNhanVien}
                onChange={(e) =>
                  setForm({ ...form, maNhanVien: e.target.value })
                }
              >
                <option value="">-- Chọn nhân viên --</option>
                {employees.map((emp) => (
                  <option key={emp.maNhanVien} value={emp.maNhanVien}>
                    {emp.hoTen} ({emp.maNhanVien})
                  </option>
                ))}
              </select>
            </div>
          </div>

          <div className="form-section-title">
            <span>Chi tiết trang phục</span>
            <button
              type="button"
              className="btn btn-ghost btn-sm"
              onClick={addDetail}
            >
              ＋ Thêm dòng
            </button>
          </div>

          <div className="detail-list">
            {form.chiTiet.map((d, i) => {
              const availableSizes = getSizesOfCostume(d.trangPhucId);
              return (
                <div key={i} className="detail-item">
                  <div className="detail-item-num">{i + 1}</div>
                  <div className="detail-item-fields detail-item-fields-4col">
                    {/* Cột 1: Chọn trang phục */}
                    <select
                      required
                      value={d.trangPhucId}
                      onChange={(e) =>
                        updateDetail(i, "trangPhucId", e.target.value)
                      }
                    >
                      <option value="">-- Trang phục --</option>
                      {costumes.map((c) => (
                        <option key={c.maTrangPhuc} value={c.maTrangPhuc}>
                          {c.tenTrangPhuc} ({c.maTrangPhuc})
                        </option>
                      ))}
                    </select>

                    {/* Cột 2: Chọn size (hiển thị sau khi chọn trang phục) */}
                    <select
                      required
                      value={d.kichThuocId}
                      onChange={(e) =>
                        updateDetail(i, "kichThuocId", e.target.value)
                      }
                      disabled={!d.trangPhucId}
                    >
                      <option value="">-- Kích thước --</option>
                      {availableSizes.map((ks) => (
                        <option key={getSizeId(ks)} value={getSizeId(ks)}>
                          {getSizeName(ks)} (tồn: {ks.soLuong})
                        </option>
                      ))}
                    </select>

                    {/* Cột 3: Số lượng */}
                    <input
                      type="number"
                      min="1"
                      value={d.soLuong}
                      onChange={(e) =>
                        updateDetail(i, "soLuong", e.target.value)
                      }
                      placeholder="SL"
                    />

                    {/* Cột 4: Đơn giá */}
                    <input
                      type="number"
                      min="0"
                      value={d.donGiaNhap}
                      onChange={(e) =>
                        updateDetail(i, "donGiaNhap", e.target.value)
                      }
                      placeholder="Đơn giá nhập"
                    />

                    <span className="detail-subtotal">
                      ={" "}
                      {new Intl.NumberFormat("vi-VN").format(
                        (parseInt(d.soLuong) || 0) *
                          (parseFloat(d.donGiaNhap) || 0),
                      )}
                      đ
                    </span>
                  </div>
                  {form.chiTiet.length > 1 && (
                    <button
                      type="button"
                      className="btn-icon btn-delete"
                      onClick={() => removeDetail(i)}
                    >
                      ✕
                    </button>
                  )}
                </div>
              );
            })}
          </div>

          <div className="total-row">
            <span>Tổng tiền:</span>
            <strong>
              {new Intl.NumberFormat("vi-VN", {
                style: "currency",
                currency: "VND",
              }).format(tongTien)}
            </strong>
          </div>

          <div className="form-actions">
            <button
              type="button"
              className="btn btn-ghost"
              onClick={() => setShowCreate(false)}
            >
              Hủy
            </button>
            <button
              type="submit"
              className="btn btn-primary"
              disabled={formLoading}
            >
              {formLoading ? "Đang tạo..." : "Tạo hóa đơn"}
            </button>
          </div>
        </form>
      </Modal>

      {/* Detail Modal */}
      <Modal
        isOpen={showDetail}
        onClose={() => setShowDetail(false)}
        title={`Chi tiết hóa đơn ${detailItem?.maHDNhap || ""}`}
      >
        {detailItem && (
          <div>
            <div className="detail-grid">
              {[
                ["Mã hóa đơn", detailItem.maHDNhap],
                ["Nhà cung cấp", detailItem.nhaCungCap?.tenNCC || "—"],
                ["Nhân viên phụ trách", detailItem.nhanVien?.hoTen || "—"],
                ["Ngày nhập", fmtDate(detailItem.ngayNhap)],
                ["Tổng tiền", fmtPrice(detailItem.tongTien)],
                [
                  "Trạng thái",
                  STATUS_BADGE[detailItem.trangThaiThanhToan]?.label ||
                    detailItem.trangThaiThanhToan,
                ],
                [
                  "Phương thức thanh toán",
                  detailItem.phuongThucThanhToan === "TIEN_MAT"
                    ? "Thanh toán tiền mặt"
                    : detailItem.phuongThucThanhToan === "CHUYEN_KHOAN"
                      ? "Chuyển khoản ngân hàng"
                      : detailItem.phuongThucThanhToan === "THE"
                        ? "Thẻ nội địa / Quốc tế"
                        : "Đang chờ thanh toán",
                ],
              ].map(([k, v]) => (
                <div key={k} className="detail-row">
                  <span className="detail-label">{k}</span>
                  <span className="detail-value">{v}</span>
                </div>
              ))}
            </div>
            {detailItem.chiTietHDNhap?.length > 0 && (
              <div>
                <h4 className="sub-section-title">Danh sách trang phục nhập</h4>
                <table className="data-table mini-table">
                  <thead>
                    <tr>
                      <th>Mã TP</th>
                      <th>Kích thước</th>
                      <th>Số lượng</th>
                      <th>Đơn giá</th>
                      <th>Thành tiền</th>
                    </tr>
                  </thead>
                  <tbody>
                    {detailItem.chiTietHDNhap.map((ct, i) => (
                      <tr key={i}>
                        <td>
                          <code className="code-cell">
                            {ct.trangPhuc?.maTrangPhuc}
                          </code>
                        </td>
                        <td>
                          {ct.kichThuoc ? (
                            <span className="size-tag">
                              {getSizeName(
                                ct.kichThuoc
                                  ? {
                                      kichThuoc: ct.kichThuoc,
                                      tenKichThuoc: ct.kichThuoc?.kichThuoc,
                                    }
                                  : null,
                              )}
                            </span>
                          ) : (
                            <span className="text-muted">—</span>
                          )}
                        </td>
                        <td>{ct.soLuong}</td>
                        <td>{fmtPrice(ct.donGiaNhap)}</td>
                        <td className="price-cell">{fmtPrice(ct.thanhTien)}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </div>
        )}
      </Modal>

      {/* Pay Modal */}
      <Modal
        isOpen={showPayModal}
        onClose={() => setShowPayModal(false)}
        title="Xác nhận thanh toán"
      >
        <div className="form-grid">
          <div className="pay-info">
            <div className="pay-info-row">
              <span>Hóa đơn</span>
              <code className="code-cell">{payTarget?.maHDNhap}</code>
            </div>
            <div className="pay-info-row">
              <span>Tổng tiền</span>
              <strong className="pay-amount">
                {payTarget ? fmtPrice(payTarget.tongTien) : ""}
              </strong>
            </div>
          </div>
          <div className="form-row">
            <label>Phương thức thanh toán *</label>
            <div className="pay-methods">
              {PHUONG_THUC_OPTIONS.map((opt) => (
                <button
                  key={opt.value}
                  type="button"
                  className={`pay-method-btn ${phuongThuc === opt.value ? "active" : ""}`}
                  onClick={() => setPhuongThuc(opt.value)}
                >
                  {opt.label}
                </button>
              ))}
            </div>
          </div>
          <div className="form-actions">
            <button
              className="btn btn-ghost"
              onClick={() => setShowPayModal(false)}
            >
              Hủy
            </button>
            <button
              className="btn btn-primary"
              onClick={handlePay}
              disabled={!!payLoading}
            >
              {payLoading ? "Đang xử lý..." : "Xác nhận thanh toán"}
            </button>
          </div>
        </div>
      </Modal>
    </div>
  );
}
