import React from "react";
import { Routes, Route } from "react-router-dom";
import PaymentList from "./pages/PaymentPage/PaymentPage.PaymentList";
import PaymentPage from "./pages/PaymentPage/PaymentPage";

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/payment/qr" element={<PaymentPage />} />
        <Route path="/payment/list" element={<PaymentList />} />
      </Routes>
    </div>
  );
}

export default App;
