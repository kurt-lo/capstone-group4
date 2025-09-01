import React from "react";
import { Link, useNavigate } from "react-router-dom";
import useAuthStore from "../authentication/useAuthStore";
import { Package, Users, ShieldCheck, LogOut } from "lucide-react";

function Navbar() {
  const navigate = useNavigate();
  const logout = useAuthStore((state) => state.logout);
  const token = useAuthStore((state) => state.user?.token);
  const role = useAuthStore((state) => state.user?.role);

  const handleLogout = () => {
    // const currentRole = role;
    // navigate(role === "admin" ? "/admin/login" : "/user/login");
    navigate("/");
    logout();
  };

  return (
    <div className="navbar sticky top-0 z-40 bg-gradient-to-r from-base-100/95 to-base-200/95 backdrop-blur-md border-b border-base-300 shadow-sm">
      {/* Left Section */}
      <div className="navbar-start gap-2">
        <Link
          to={"/"}
          className="btn btn-ghost normal-case text-2xl font-extrabold tracking-tight hover:bg-primary/10 transition"
        >
          <Package className="size-6 text-primary" />
          <span className="ml-2">CargoTrack</span>
        </Link>
        <div className="hidden md:flex items-center gap-2 text-xs px-3 py-1 rounded-full bg-base-200/80 border border-base-300">
          <ShieldCheck className="size-4 text-success" />
          <span className="font-medium">Enterprise</span>
        </div>
      </div>

      {/* Center Menu (when logged in) */}
      {token && (
        <div className="navbar-center hidden lg:flex">
          <ul className="menu menu-horizontal px-1 gap-2">
            <li>
              <Link to={'/dashboard'} className="font-medium hover:text-primary hover:underline underline-offset-4 transition">
                Dashboard
              </Link>
            </li>
            <li>
              <Link to={'/report'} className="font-medium hover:text-primary hover:underline underline-offset-4 transition">
                Reports
              </Link>
            </li>
          </ul>
        </div>
      )}

      {/* Right Section */}
      <div className="navbar-end gap-2">
        {!token && (
          <>
            <Link
              className="btn btn-primary btn-sm px-4 font-medium shadow-md hover:shadow-lg transition"
              to={`/${role ? role : "user"}/login`}
            >
              Sign in
            </Link>
            <Link
              className="btn btn-outline btn-sm px-4 font-medium hover:bg-primary/10 transition"
              to={`/${role ? role : "user"}/register`}
            >
              Sign up
            </Link>
          </>
        )}
        {token && (
          <>
            <Link to={"/profile"} className="btn btn-ghost btn-circle hover:bg-primary/10 transition">
              <Users className="size-5 text-base-content/70" />
            </Link>
            <button
              onClick={handleLogout}
              className="btn btn-ghost btn-circle hover:bg-error/10 transition"
            >
              <LogOut className="size-5 text-error" />
            </button>
          </>
        )}
      </div>
    </div>
  );
}

export default Navbar;
