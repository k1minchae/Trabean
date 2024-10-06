import { Routes, Route } from "react-router-dom";
import PaymentHistory from "./pages/PaymentPage/PaymentPage.PaymentHistory";
import PaymentPage from "./pages/PaymentPage/PaymentPage";
import PaymentPassword from "./pages/PaymentPage/PaymentPage.Password";
import PaymentSuccessPage from "./pages/PaymentPage/PaymentPage.Payment.SuccessPage";
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
import CurrencyAddPage from "./pages/AccountCreationPage/CurrencyAddPage";
import MapTestPage from "./pages/AccountCreationPage/MapTestPage";
import ExchangeRates from "./pages/Exchange/ExchangePage";
import ExchangeSplit from "./pages/SplitPage/SplitPage";
import PasswordPage from "./pages/AccountCreationPage/PasswordPage";
import SuccessPage from "./pages/TransferPage/TransferPage.successPage";
import BottomBar from "./components/BottomBar";
import Notification from "./pages/NotificationPage/NotificationPage";
import DomesticTravelAccountPage from "./pages/TravelAccount/page/DomesticTravelAccountPage";
import DomesticTravelAccountDetailPage from "./pages/TravelAccount/page/DomesticTravelAccountDetailPage";
import ForeignTravelAccountDetailPage from "./pages/TravelAccount/page/ForeignTravelAccountDetailPage";
import ChargeForeignTravelAccountPage from "./pages/TravelAccount/page/ChargeForeignTravelAccountPage";
import CreateForeignTravelAccountPage from "./pages/TravelAccount/page/CreateForeignTravelAccountPage";
import MemberManagementPage from "./pages/TravelAccount/page/MemberManagementPage";

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route
          path="/accounts/travel/domestic/:accountId"
          element={<DomesticTravelAccountPage />}
        />
        <Route
          path="/accounts/travel/domestic/:accountId/detail"
          element={<DomesticTravelAccountDetailPage />}
        />
        <Route
          path="/accounts/travel/foreign/:accountId/detail"
          element={<ForeignTravelAccountDetailPage />}
        />
        <Route
          path="/accounts/travel/domestic/:accountId/members"
          element={<MemberManagementPage />}
        />
        <Route
          path="/accounts/travel/foreign/:parentAccountId/create"
          element={<CreateForeignTravelAccountPage />}
        />
        <Route
          path="/accounts/travel/foreign/:parentAccountId/charge"
          element={<ChargeForeignTravelAccountPage />}
        />

        <Route path="/payment/qr" element={<PaymentPage />} />
        <Route
          path="/payment/qr/:merchantId/:merchantName/:currency/:amount"
          element={<PaymentPassword />}
        />
        <Route
          path="/payment/qr/success/:payId"
          element={<PaymentSuccessPage />}
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
        <Route path="/creation/travel/currency" element={<CurrencyAddPage />} />
        <Route path="/chatbot/map" element={<MapTestPage />} />
        <Route path="/exchange" element={<ExchangeRates />} />
        <Route path="/travel/split" element={<ExchangeSplit />} />
        <Route path="/transfer/list" element={<TransferList />} />
        <Route path="transfer/password" element={<PasswordPage />} />
        <Route path="transfer/success" element={<SuccessPage />} />
        <Route path="/notification" element={<Notification />} />
      </Routes>
      <BottomBar />
    </div>
  );
}

export default App;
