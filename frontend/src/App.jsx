import {
  BrowserRouter,
  Routes,
  Route,
  NavLink,
  Navigate,
} from "react-router-dom";
import { useToast, ToastContainer } from "./components/ThongBaoNhanh";
import TrangPhucPage from "./pages/TrangPhucPage";
import NhapTrangPhucPage from "./pages/NhapTrangPhucPage";
import "./App.css";

function Layout({ children }) {
  return (
    <div className="app-layout">
      <aside className="sidebar">
        <div className="sidebar-brand">
          <div>
            <div className="brand-name">CostumeMS</div>
            <div className="brand-sub">Quản lý trang phục</div>
          </div>
        </div>
        <nav className="sidebar-nav">
          <div className="nav-section-label">Chức năng chính</div>
          <NavLink
            to="/costumes"
            className={({ isActive }) => `nav-item ${isActive ? "active" : ""}`}
          >
            Quản lý trang phục
          </NavLink>
          <NavLink
            to="/imports"
            className={({ isActive }) => `nav-item ${isActive ? "active" : ""}`}
          >
            Nhập trang phục
          </NavLink>
        </nav>
        <div className="sidebar-footer">
          <div className="footer-info">
            <span className="gateway-dot" />
            Quản lý cho thuê trang phục
          </div>
        </div>
      </aside>
      <main className="main-content">{children}</main>
    </div>
  );
}

export default function App() {
  const { toasts } = useToast();

  return (
    <BrowserRouter>
      <ToastContainer toasts={toasts} />
      <Layout>
        <Routes>
          <Route path="/" element={<Navigate to="/costumes" replace />} />
          <Route path="/costumes" element={<TrangPhucPage />} />
          <Route path="/imports" element={<NhapTrangPhucPage />} />
        </Routes>
      </Layout>
    </BrowserRouter>
  );
}
