import { Route, Routes } from "react-router";
import "./App.css";
import Home from "./components/Home";
import Navbar from "./components/Navbar";
import Footer from "./components/Footer";
import ContainerDetails from "./components/ContainerDetails";
import Containers from "./components/Containers";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";

function App() {
  return (
    <div className="min-h-screen">
    <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/containers" element={<Containers />} />
        <Route path="/containers/:id" element={<ContainerDetails />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
      </Routes>
    {/* <Footer /> */}
    </div>
  );
}

export default App;
