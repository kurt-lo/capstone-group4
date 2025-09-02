import { Link, useLocation, useNavigate } from "react-router-dom";
import useAuthStore, { ONE_DAY } from "../authentication/useAuthStore";
import axios from "axios";
import { toast } from "react-toastify";
import Cookies from "js-cookie";
import React, { useState, useEffect } from "react";
import shipImg from "../assets/ship.jpg";

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

      navigate("/dashboard");
    } catch (err) {
      console.error(err);
      toast.error("Invalid Email or Password!");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 h-screen w-full">
      <div className="bg-gray-800 flex flex-col justify-center">
        <form
          className="max-w-[400px] w-full mx-auto rounded-lg bg-gray-900 p-8 px-8 shadow-lg shadow-gray-500/50"
          onSubmit={handleSubmit}
        >
          <h2 className="text-4xl text-white font-bold text-center">
            Welcome, {role === "admin" ? "Admin" : "User"}!
          </h2>
          <div className="flex flex-col text-gray-400 py-2">
            <label>Username</label>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="rounded-lg bg-gray-600 mt-2 p-2 focus:border-blue-500 focus:bg-gray-800 focus:outline-none"
            />
          </div>
          <div className="flex flex-col text-gray-400 py-2">
            <label>Password</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="rounded-lg bg-gray-600 mt-2 p-2 focus:border-blue-500 focus:bg-gray-800 focus:outline-none"
            />
          </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full my-5 py-2 bg-white hover:bg-gray-600 shadow-lg shadow-gray-500/10 hover:shadow-black text-gray-900 hover:text-white font-semibold rounded-lg"
            >
              {loading ? "Logging in..." : "Log In"}
            </button>

            <p className="text-gray-400 text-sm text-center">
              Dont have an account?{" "}
              <Link
                className="text-white underline"
                to={`/${role}/register`}
              >
                Sign up!
              </Link>
            </p>
        </form>
      </div>

      <div className="hidden md:block">
        <img
          className="w-full h-full object-cover"
          src={shipImg}
          alt="vessel-pic"
        />
      </div>
    </div>
  );
}

export default LoginPage;
