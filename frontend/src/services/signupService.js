import axios from "axios";
const API_BASE_URL = "http://localhost:8080/api/users";
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});
export const signupService = {
  signupUser: (user) => api.post("/signup", user),
};
