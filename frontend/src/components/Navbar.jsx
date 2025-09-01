import React from "react";
import { useNavigate } from "react-router-dom";
import useAuthStore from "../authentication/useAuthStore";

function Navbar() {
  const navigate = useNavigate();
  const logout = useAuthStore((state) => state.logout);
  const user = useAuthStore((state) => state.user);

  const handleLogout = () => {
    logout();
    navigate(user?.role === "admin" ? "/admin/login" : "/user/login");
  };

  return (
    <nav className="p-4 bg-gray-800 text-white flex justify-between">
      <div>My App</div>
      {user && (
        <button
          onClick={handleLogout}
          className="bg-red-600 px-4 py-1 rounded hover:bg-red-500"
        >
          Logout
        </button>
      )}
    </nav>
  );
}

export default Navbar;
