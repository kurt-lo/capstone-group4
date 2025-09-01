import { useEffect, useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import useAuthStore, { ONE_DAY } from "../authentication/useAuthStore";
import axios from "axios";
import { toast } from "react-toastify";
import Cookies from "js-cookie";

function LoginPage() {
  const navigate = useNavigate();
  const location = useLocation();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [role, setRole] = useState("user");

  const login = useAuthStore((state) => state.login);

  useEffect(() => {
    if (location.pathname.startsWith("/admin")) {
      setRole("admin");
    } else {
      setRole("user");
    }
  }, [location.pathname]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const endpoint =
        role === "admin"
          ? "http://localhost:9090/api/admin/login"
          : "http://localhost:9090/api/user/login";

      const response = await axios.post(endpoint, {
        username,
        password,
      });

      const data = await response.data;
      // const userData = data.username;
      // const authToken = data.token;
      console.log("");

      const userData = { username: data.username, role };
      login(userData, data.token);

      Cookies.set("authUserToken", data.token, { expires: ONE_DAY });
      Cookies.set("authUserRole", role, { expires: ONE_DAY });
      Cookies.set("authUserName", data.username, { expires: ONE_DAY });

      toast.success("Login success!");

      // navigate(role === "admin" ? "/admin/dashboard" : "/dashboard");
      navigate("/dashboard");
    } catch (err) {
      console.error(err);
      toast.error("Invalid Email or Password!");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-gray-300">
      <div className="w-full max-w-sm rounded-xl bg-[#EAF4F6] p-8 shadow-lg">
        <h1 className="mb-6 text-4xl font-bold text-gray-700">
          Welcome {role === "admin" ? "Admin" : "User"}!
        </h1>

        <form className="space-y-4" onSubmit={handleSubmit}>
          <input
            type="text"
            placeholder="Username:"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            className="w-full rounded-lg bg-[#D9B6C2] px-4 py-3 text-white placeholder-white focus:outline-none"
          />
          <input
            type="password"
            placeholder="Password:"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="w-full rounded-lg bg-[#D9B6C2] px-4 py-3 text-white placeholder-white focus:outline-none"
          />

          <div className="flex items-center justify-between pt-2">
            <Link
              className="text-sm text-gray-600 hover:underline"
              to={`/${role}/register`}
            >
              Sign up!
            </Link>
            <button
              type="submit"
              disabled={loading}
              className="rounded-md bg-[#B56E6E] px-6 py-2 text-white font-medium hover:bg-[#9e5e5e] disabled:opacity-50"
            >
              {loading ? "Logging in..." : "Log In"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default LoginPage;
