import { Navigate, Outlet } from "react-router-dom";
import Cookies from "js-cookie";
import { jwtDecode } from "jwt-decode";

const ProtectedRoute = ({ allowedRole }) => {
  const token = Cookies.get("authUserToken");

  if (!token) {
    return <Navigate to={`/${allowedRole || "user"}/login`} replace />;
  }

  try {
    const decoded = jwtDecode(token);
    const currentTime = Math.floor(Date.now() / 1000);

    // Check token expiration
    if (decoded.exp && decoded.exp < currentTime) {
      Cookies.remove("authUserToken");
      Cookies.remove("authUserRole");
      Cookies.remove("authUserName");
      return <Navigate to={`/${allowedRole || "user"}/login`} replace />;
    }

    // Role check
    if (allowedRole && decoded.role.toLowerCase() !== allowedRole.toLowerCase()) {
      return <Navigate to={`/${decoded.role.toLowerCase()}/dashboard`} replace />;
    }

    return <Outlet />;
  } catch (err) {
    // Invalid token
    Cookies.remove("authUserToken");
    Cookies.remove("authUserRole");
    Cookies.remove("authUserName");
    return <Navigate to={`/${allowedRole || "user"}/login`} replace />;
  }
};

export default ProtectedRoute;
