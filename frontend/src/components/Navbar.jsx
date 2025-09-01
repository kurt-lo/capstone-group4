import React from "react";
import { Link, useNavigate } from "react-router-dom";
import useAuthStore from "../authentication/useAuthStore";

function Navbar() {
  const navigate = useNavigate();
  const logout = useAuthStore((state) => state.logout);
  const token = useAuthStore((state) => state.user?.token);
  const role = useAuthStore((state) => state.user?.role);

  const handleLogout = () => {
    logout();
    navigate(user?.role === "admin" ? "/admin/login" : "/user/login");
  };

  return (
    <nav className="items-center p-4 bg-gray-800 text-white flex justify-between">
      <Link to={"/"}>Container Management System</Link>
      {!token && (
        <div className="flex gap-[1rem]">
          <Link
            to={`/${role ? role : "user"}/login`}
            className="btn btn-outline"
          >
            Login
          </Link>
          <Link
            to={`/${role ? role : "user"}/register`}
            className="btn btn-outline"
          >
            Register
          </Link>
        </div>
      )}
      {token && (
        <button
          onClick={handleLogout}
          className="bg-red-600 px-4 py-1 rounded hover:bg-red-400 cursor-pointer"
        >
          Logout
        </button>
      )}
    </nav>
  );
}

export default Navbar;
