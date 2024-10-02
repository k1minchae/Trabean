import { Routes, Route } from "react-router-dom";

import PaymentHistory from "./pages/PaymentPage/PaymentPage.PaymentHistory";
import PaymentPage from "./pages/PaymentPage/PaymentPage";
import LoginPage from "./pages/LoginPage/LoginPage";
import MainPage from "./pages/MainPage/MainPage";

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/payment/qr" element={<PaymentPage />} />
        <Route path="/payment/list" element={<PaymentHistory />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/" element={<MainPage />} />
      </Routes>
    </div>
  );
}

export default App;
