import { Routes, Route } from "react-router-dom";

import PaymentList from "./pages/PaymentPage/PaymentPage.PaymentList";
import PaymentPage from "./pages/PaymentPage/PaymentPage";
import LoginPage from "./pages/LoginPage/LoginPage";

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/payment/qr" element={<PaymentPage />} />
        <Route path="/payment/list" element={<PaymentList />} />
        <Route path="/login" element={<LoginPage />} />
      </Routes>
    </div>
  );
}

export default App;
