import { create } from "zustand";

interface AuthState {
  accessToken: string | null;
  paymentAccountId: string | null;
  setAccessToken: (token: string | null) => void;
  setPaymentAccountId: (id: string | null) => void;
}

const useAuthStore = create<AuthState>((set) => ({
  accessToken: localStorage.getItem("accessToken"),
  paymentAccountId: localStorage.getItem("paymentAccountId"),
  setAccessToken: (token) => {
    if (token) {
      localStorage.setItem("accessToken", token);
    } else {
      localStorage.removeItem("accessToken");
    }
    set({ accessToken: token });
  },
  setPaymentAccountId: (id) => {
    if (id) {
      localStorage.setItem("paymentAccountId", id);
    } else {
      localStorage.removeItem("paymentAccountId");
    }
    set({ paymentAccountId: id });
  },
}));

export default useAuthStore;
