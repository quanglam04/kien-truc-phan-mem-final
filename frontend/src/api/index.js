import axios from "axios";

const BASE_URL = "http://localhost:8080";

const api = axios.create({ baseURL: BASE_URL });

export const costumeApi = {
  getAll: () => api.get("/api/costumes"),
  getById: (id) => api.get(`/api/costumes/${id}`),
  create: (data) => api.post("/api/costumes", data),
  update: (id, data) => api.put(`/api/costumes/${id}`, data),
  delete: (id) => api.delete(`/api/costumes/${id}`),
  getSizeQuantities: (id) => api.get(`/api/costumes/${id}/sizes`),
  updateSizeQuantity: (id, sizeId, delta) =>
    api.put(`/api/costumes/${id}/sizes/${sizeId}/quantity`, null, {
      params: { delta },
    }),
};

export const supplierApi = {
  getAll: () => api.get("/api/suppliers"),
  getById: (id) => api.get(`/api/suppliers/${id}`),
  create: (data) => api.post("/api/suppliers", data),
  update: (id, data) => api.put(`/api/suppliers/${id}`, data),
  delete: (id) => api.delete(`/api/suppliers/${id}`),
  getStats: () => api.get("/api/suppliers/stats"),
};

export const employeeApi = {
  getAll: () => api.get("/api/employee-warehouse"),
};

export const importApi = {
  getAll: () => api.get("/api/imports"),
  getById: (id) => api.get(`/api/imports/${id}`),
  create: (data) => api.post("/api/imports", data),
  pay: (id, data) => api.put(`/api/imports/${id}/pay`, data),
};

export const loaiTrangPhucApi = {
  getAll: () => api.get("/api/loai-trang-phuc"),
  create: (data) => api.post("/api/loai-trang-phuc", data),
};

export const kichThuocApi = {
  getAll: () => api.get("/api/kich-thuoc"),
};
