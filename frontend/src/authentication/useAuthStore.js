import { create } from 'zustand';
import { persist } from 'zustand/middleware'; // Optional: for persistent login

const useAuthStore = create(
  persist( // will persist  the state of these despite the page reloads or the server restarts
    (set) => ({
      user: null,
      token: null,
      login: (userData, authToken) => set({ user: userData, token: authToken }),
      logout: () => set({ user: null, token: null }),
      setUser: (userData) => set({ user: userData }),
      setToken: (authToken) => set({ token: authToken }),
    }),
    {
      name: 'authUserToken', // Name for localStorage key
      getStorage: () => localStorage, // Use localStorage for persistence
    }
  )
);

export default useAuthStore;