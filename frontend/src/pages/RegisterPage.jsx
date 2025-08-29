import React, { useState } from "react";
import shipImg from "../assets/ship.jpg";
import axios from "axios";
import { toast } from "react-toastify";

function RegisterPage() {
  // useState hook to manage the form data.
  const [signupData, setSignupData] = useState({
    firstName: "",
    lastName: "",
    emailAddress: "",
    companyName: "",
    username: "",
    password: "",
    userRole: "USER",
  });

  const [confirmPassword, setConfirmPassword] = useState("");

  // useState hook to manage the form submission state.
  const [errorMessage, setErrorMessage] = useState("");

  // Loading Message
  const [loadingMessage, setLoadingMessage] = useState("");

  // Password Error Message
  const [passwordError, setPasswordError] = useState("");

  // Handles changes to form inputs and updates the state.
  const handleChange = (e) => {
    const { name, value } = e.target;
    setSignupData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  // Password Validation

  const validatePassword = (password) => {
    if (password.length < 8 || confirmPassword.length < 8) {
      setPasswordError("Password must be at least 8 characters long.");
    } else if (!/[A-Z]/.test(password) || !/[a-z]/.test(password) || !/[0-9]/.test(password)) {
      setPasswordError("Password must contain at least one uppercase letter, lowercase letter and a number.");
    } else if (password !== confirmPassword) {
      setPasswordError("Password don't match!");
    } 
    return passwordError === "";
  };

  // Handles the form submission.
  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoadingMessage("Submitting...");


    try {
      // TO DO: call axios find by email and username for validation
      // if () {
      //   toast.error('Email already exist!');
      // }
      
      const isValid = validatePassword(signupData.password)
      
      if (isValid) {
        
        const response = await axios.post(
          "http://localhost:9090/api/user/register",
          signupData,
          { headers: { "Content-Type": "application/json" } }
        );

        if (response.status === 201) {
          setLoadingMessage("");
          toast.success("User successfully registered!");

          // Clear the form after a successful submission.
          setSignupData({
            firstName: "",
            lastName: "",
            companyName: "",
            emailAddress: "",
            username: "",
            password: "",
          });

          setConfirmPassword("");

        } else {
          setErrorMessage("Error in signing up. Please try again.");
        }
        
      } 
    } catch (error) {
      console.error("Error:", error);
      setErrorMessage("Network error. Could not connect to the server.");
    }
  };

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 h-screen w-full">
      <div className="hidden sm:block">
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
          <h2 className="text-4xl text-white font-bold text-center">Sign Up</h2>

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
          <div className="flex justify-between text-gray-400 py-2">
            <p className="flex items-center">
              <input className="mr-2" type="checkbox" required /> I agree
              to&nbsp;
              <a href="" className="text-white">
                Terms and Conditions
              </a>
            </p>
          </div>
          {errorMessage && (
            <p className="message text-red-400 py-2">{errorMessage}</p>
          )}
          {loadingMessage && (
            <p className="message text-blue-400 py-2">{loadingMessage}</p>
          )}
          <button
            type="submit"
            className="w-full my-5 py-2 bg-white hover:bg-gray-600 shadow-lg shadow-gray-500/10 hover:shadow-gray-500/100 text-gray-900 hover:text-white font-semibold rounded-lg"
          >
            Sign Up
          </button>
        </form>
      </div>
    </div>
  );
}
export default RegisterPage;
