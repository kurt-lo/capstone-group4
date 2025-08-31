import { create } from "zustand";
import Cookies from "js-cookie";

export const ONE_DAY = 1;

const useAuthStore = create((set) => {
  const token = Cookies.get("authUserToken") || null;
  const role = Cookies.get("authUserRole") || null;
  const username = Cookies.get("authUserName") || null;

  return {
    user: token ? { token, role, username } : null,

    login: (userData, token) => {
      set({ user: { ...userData, token } });
      Cookies.set("authUserToken", token, { expires: ONE_DAY });
      Cookies.set("authUserRole", userData.role, { expires: ONE_DAY });
      Cookies.set("authUserName", userData.username, { expires: ONE_DAY });
    },

    logout: () => {
      set({ user: null });
      Cookies.remove("authUserToken");
      Cookies.remove("authUserRole");
      Cookies.remove("authUserName");
    },

    isAuthenticated: () => !!Cookies.get("authUserToken"),
  };
});

export default useAuthStore;
