import React, { useState, useEffect } from "react";
import shipImg from "../assets/ship.jpg";
import axios from "axios";
import { toast } from "react-toastify";
import { useLocation, useNavigate, Link } from "react-router-dom";

function RegisterPage() {
  const navigate = useNavigate();
  const location = useLocation();

  const [role, setRole] = useState("user");

  const [signupData, setSignupData] = useState({
    firstName: "",
    lastName: "",
    emailAddress: "",
    companyName: "",
    username: "",
    password: "",
  });

  const [confirmPassword, setConfirmPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [loadingMessage, setLoadingMessage] = useState("");
  const [passwordError, setPasswordError] = useState("");

  useEffect(() => {
    if (location.pathname.startsWith("/admin")) setRole("admin");
    else setRole("user");
  }, [location.pathname]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setSignupData((prev) => ({ ...prev, [name]: value }));
  };

  const validatePassword = () => {
    const { password } = signupData;
    if (password.length < 8) {
      setPasswordError("Password must be at least 8 characters long.");
      return false;
    }
    if (
      !/[A-Z]/.test(password) ||
      !/[a-z]/.test(password) ||
      !/[0-9]/.test(password)
    ) {
      setPasswordError(
        "Password must contain at least one uppercase letter, lowercase letter and a number."
      );
      return false;
    }
    if (password !== confirmPassword) {
      setPasswordError("Passwords do not match!");
      return false;
    }
    setPasswordError("");
    return true;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrorMessage("");
    setLoadingMessage("Submitting...");

    if (!validatePassword()) {
      setLoadingMessage("");
      return;
    }

    try {
      const endpoint =
        role === "admin"
          ? "http://localhost:9090/api/admin/register"
          : "http://localhost:9090/api/user/register";

      const response = await axios.post(endpoint, signupData, {
        headers: { "Content-Type": "application/json" },
      });

      if (response.status === 201 || response.status === 200) {
        setLoadingMessage("");
        toast.success(
          `${
            role.charAt(0).toUpperCase() + role.slice(1)
          } registered successfully!`
        );

        setSignupData({
          firstName: "",
          lastName: "",
          emailAddress: "",
          companyName: "",
          username: "",
          password: "",
        });
        setConfirmPassword("");

        navigate(`/${role}/login`);
      } else {
        setErrorMessage("Error in signing up. Please try again.");
      }
    } catch (error) {
      console.error(error);
      setErrorMessage("Network error. Could not connect to the server.");
    } finally {
      setLoadingMessage("");
    }
  };

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 h-screen w-full">
      <div className="hidden md:block">
        <img
          className="w-full h-full object-cover"
          src={shipImg}
          alt="vessel-pic"
        />
      </div>

      <div className="bg-gray-800 flex flex-col justify-center">
        <form
          onSubmit={handleSubmit}
          className="max-w-[400px] w-full mx-auto rounded-lg bg-gray-900 p-8 px-8 shadow-lg shadow-gray-500/50"
        >
          <h2 className="text-4xl text-white font-bold text-center">
            Sign Up as {role === "admin" ? "Admin" : "User"}
          </h2>

          <div className="grid gap-4 mt-2 md:grid-cols-2 py-4 text-gray-400">
            <div className="flex flex-col">
              <label>First Name</label>
              <input
                type="text"
                name="firstName"
                value={signupData.firstName}
                onChange={handleChange}
                required
                className="rounded-lg bg-gray-600 mt-2 p-2 focus:border-blue-500 focus:bg-gray-800 focus:outline-none"
              />
            </div>
            <div className="flex flex-col">
              <label>Last Name</label>
              <input
                type="text"
                name="lastName"
                value={signupData.lastName}
                onChange={handleChange}
                required
                className="rounded-lg bg-gray-600 mt-2 p-2 focus:border-blue-500 focus:bg-gray-800 focus:outline-none"
              />
            </div>
          </div>

          <div className="flex flex-col text-gray-400 py-2">
            <label>Company Name</label>
            <input
              type="text"
              name="companyName"
              value={signupData.companyName}
              onChange={handleChange}
              required
              className="rounded-lg bg-gray-600 mt-2 p-2 focus:border-blue-500 focus:bg-gray-800 focus:outline-none"
            />
          </div>

          <div className="flex flex-col text-gray-400 py-2">
            <label>Email</label>
            <input
              type="email"
              name="emailAddress"
              value={signupData.emailAddress}
              onChange={handleChange}
              required
              className="rounded-lg bg-gray-600 mt-2 p-2 focus:border-blue-500 focus:bg-gray-800 focus:outline-none"
            />
          </div>

          <div className="flex flex-col text-gray-400 py-2">
            <label>Username</label>
            <input
              type="text"
              name="username"
              value={signupData.username}
              onChange={handleChange}
              required
              className="rounded-lg bg-gray-600 mt-2 p-2 focus:border-blue-500 focus:bg-gray-800 focus:outline-none"
            />
          </div>

          <div className="flex flex-col text-gray-400 py-2">
            <label>Password</label>
            <input
              type="password"
              name="password"
              value={signupData.password}
              onChange={handleChange}
              required
              className="p-2 rounded-lg bg-gray-600 mt-2 focus:border-blue-500 focus:bg-gray-800 focus:outline-none"
            />
          </div>

          <div className="flex flex-col text-gray-400 py-2">
            <label>Confirm Password</label>
            <input
              type="password"
              name="confirmPassword"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
              className="p-2 rounded-lg bg-gray-600 mt-2 focus:border-blue-500 focus:bg-gray-800 focus:outline-none"
            />
          </div>

          {passwordError && (
            <p className="text-red-400 py-2">{passwordError}</p>
          )}
          {errorMessage && <p className="text-red-400 py-2">{errorMessage}</p>}
          {loadingMessage && (
            <p className="text-blue-400 py-2">{loadingMessage}</p>
          )}

          <button
            type="submit"
            className="w-full my-5 py-2 bg-white hover:bg-gray-600 shadow-lg shadow-gray-500/10 hover:shadow-black text-gray-900 hover:text-white font-semibold rounded-lg"
          >
            Sign Up
          </button>

          <p className="text-gray-400 text-sm text-center">
            Already have an account?{" "}
            <Link className="text-white underline" to={`/${role}/login`}>
              Log in
            </Link>
          </p>
        </form>
      </div>
    </div>
  );
}

export default RegisterPage;
