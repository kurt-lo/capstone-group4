import { useState } from "react";
import { useNavigate } from "react-router";
import useAuthStore from "../authentication/useAuthStore";
import axios from "axios";
import { toast } from "react-toastify";

function LoginPage() {
  const navigate = useNavigate();
  const [username, setUsername] = useState("");//setters getters
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const login = useAuthStore((state) => state.login);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const response = await axios.post('http://localhost:9090/api/user/login' ,{
        username,
        password
      });

      const data = await response.data;
      const userData = data.username;
      const authToken = data.token;
      console.log('');

      login(userData, authToken);
      localStorage.setItem("authUserToken", data.token);

      toast.success('Login success!');

      // navigate("/dashboard");
      
    } catch (err) {
      console.error(err);
      toast.error('Invalid Email or Password!');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex min-h-screen items-center justify-center bg-gray-300">
      <div className="w-full max-w-sm rounded-xl bg-[#EAF4F6] p-8 shadow-lg">
        <h1 className="mb-6 text-4xl font-bold text-gray-700">
          welcome<span className="text-gray-700">!</span>
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
            
            // lagay na lang si signup dito pag tapos
            <a href="#" className="text-sm text-gray-600 hover:underline">
              sign up!
            </a>
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
