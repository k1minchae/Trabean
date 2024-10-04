import { Routes, Route } from "react-router-dom";

import PaymentHistory from "./pages/PaymentPage/PaymentPage.PaymentHistory";
import PaymentPage from "./pages/PaymentPage/PaymentPage";
import PaymentPassword from "./pages/PaymentPage/PaymentPage.Password";
import TravelAccountPage from "./pages/TravelAccount/TravelAccountPage";
import LoginPage from "./pages/LoginPage/LoginPage";
import MainPage from "./pages/MainPage/MainPage";
import JoinPage from "./pages/JoinPage/JoinPage";
import MailInfo from "./pages/JoinPage/JoinPage.MailInfo";
import TransferList from "./pages/TransferPage/TransferPage.TransferList";
import TransferLists from "./pages/TransferPage/TransferPage.TransferLists";
import BankSelectionPage from "./pages/AccountCreationPage/BankSelectionPage";
import CreationMainPage from "./pages/AccountCreationPage/CreationMainPage";
import AccountAuthPage from "./pages/AccountCreationPage/AccountAuthPage";
import IdentityAuthPage from "./pages/AccountCreationPage/IdentityAuthPage";
import TravleSetupPage from "./pages/AccountCreationPage/TravelAccountSetup";

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route
          path="/accounts/travel/domestic"
          element={<TravelAccountPage />}
        />
        <Route path="/payment/qr" element={<PaymentPage />} />
        <Route
          path="/payment/qr/:payId/:merchantName/:currency/:amount"
          element={<PaymentPassword />}
        />
        <Route path="/payment/list" element={<PaymentHistory />} />
        <Route path="/join" element={<JoinPage />} />
        <Route path="/mailInfo" element={<MailInfo />} />
        <Route path="/transfer/list" element={<TransferLists />} />
        <Route path="/transfer/list/:account" element={<TransferList />} />
        <Route path="/creation/travel" element={<CreationMainPage />} />
        <Route path="/creation/travel/bank" element={<BankSelectionPage />} />
        <Route path="/creation/travel/account" element={<AccountAuthPage />} />
        <Route
          path="/creation/travel/identity"
          element={<IdentityAuthPage />}
        />
        <Route path="/creation/travel/setup" element={<TravleSetupPage />} />
      </Routes>
    </div>
  );
}

export default App;
