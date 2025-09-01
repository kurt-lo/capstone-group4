import React from "react";
import cargoImage from "../assets/container.jpg";

function HomePage() {
  return (
    <div className="min-h-screen bg-base-100">
      {/* Navbar */}
      <header className="navbar bg-base-100 shadow-md px-6">
        <div className="flex-1">
          <a className="text-xl font-bold">LOGO</a>
        </div>
        <div className="flex gap-2">
          <button className="btn btn-ghost">Sign In</button>
          <button className="btn btn-primary">Sign Up</button>
          <button className="btn btn-secondary">Contact Us</button>
        </div>
      </header>

      {/* Hero Section */}
      <section className="hero min-h-[85vh] bg-base-100 relative">
        {/* Hero Background Image */}
        <div
          className="absolute inset-0 bg-cover bg-center"
          style={{
            backgroundImage:
              "src= {cargoImage}",
          }}
        />
        {/* Overlay */}
        <div className="absolute inset-0 bg-black bg-opacity-50"></div>

        {/* Hero Content */}
        <div className="relative z-10 flex flex-col lg:flex-row items-start max-w-7xl mx-auto px-6 gap-12 h-full">
          {/* Left Side - Text */}
          <div className="flex-1 text-left text-white mt-12 lg:mt-0">
            <h1 className="text-5xl font-bold mb-4">
              Industrial Resource & Oil Exploration
            </h1>
            <p className="mb-6 text-lg">
              Delivering sustainable energy and innovative solutions for a better
              future. Trusted by leading industries worldwide.
            </p>
            <div className="flex flex-col sm:flex-row justify-start gap-4">
              <button className="btn btn-primary">Sign In</button>
              <button className="btn btn-accent">Sign Up</button>
              <button className="btn btn-outline">Contact Us</button>
            </div>
          </div>

          {/* Right Side - Image */}
          <div className="flex-1 flex justify-end items-center">
            <img
              src={cargoImage}  // <--- here, directly use the variable
              alt="Oil industry illustration"
              className="rounded-lg shadow-2xl max-w-full h-auto"
            />
          </div>
        </div>
      </section>
    </div>
  );
}

export default HomePage;
