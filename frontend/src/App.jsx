import { Route, Routes } from "react-router";
import "./App.css";
import Navbar from "./components/Navbar";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import Dashboard from "./pages/Dashboard";
import ProtectedRoute from "./components/ProtectedRoute";
import HomePage from "./pages/HomePage";
import Profile from "./pages/ProfilePage.jsx";
import ReportPage from "./pages/ReportPage.jsx";

function App() {
  return (
    <div className="min-h-screen min-w-full w-full">
      <Navbar />

      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/user/login" element={<LoginPage />} />
        <Route path="/admin/login" element={<LoginPage />} />
        <Route path="/user/register" element={<RegisterPage />} />
        <Route path="/admin/register" element={<RegisterPage />} />
        <Route element={<ProtectedRoute allowedRoles={["USER", "ADMIN"]} />}>
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/report" element={<ReportPage />} />
        </Route>
      </Routes>
      {/* <Footer /> */}
    </div>
  );
}

export default App;
