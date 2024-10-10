import { Route, Routes } from "react-router-dom";

import { AccountTypeProvider } from "./pages/AccountCreationPage/AccountTypeContext";
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
import AccountSetupPage from "./pages/AccountCreationPage/AccountSetupPage";
import CurrencyAddPage from "./pages/AccountCreationPage/CurrencyAddPage";
import MapTestPage from "./pages/AccountCreationPage/MapTestPage";
import ExchangeRates from "./pages/Exchange/ExchangePage";
import DTransferLists from "./pages/DomesticTransferPage/DTransferPage.DTransferLists";
import DTransferList from "./pages/DomesticTransferPage/DTransferPage.DTransferList";
import PasswordPage from "./pages/TransferPage/PasswordPage";
import SuccessPage from "./pages/TransferPage/TransferPage.successPage";
import BottomBar from "./components/BottomBar";
// import Notification from "./pages/NotificationPage/NotificationPage";
import DomesticTravelAccountPage from "./pages/TravelAccount/page/DomesticTravelAccountPage";
import DomesticTravelAccountDetailPage from "./pages/TravelAccount/page/DomesticTravelAccountDetailPage";
import ForeignTravelAccountDetailPage from "./pages/TravelAccount/page/ForeignTravelAccountDetailPage";
import ChargeForeignTravelAccountPage from "./pages/TravelAccount/page/ChargeForeignTravelAccountPage";
import CreateForeignTravelAccountPage from "./pages/TravelAccount/page/CreateForeignTravelAccountPage";
import MemberManagementPage from "./pages/TravelAccount/page/MemberManagementPage";
import InvitePage from "./pages/InvitePage/InvitePage";
import ProtectedRoute from "./routes/PrivateRoute";
import ChatbotPage from "./pages/ChatbotPage/ChatbotMainPage";
import ChatMapPage from "./pages/ChatbotPage/ChatMapPage";
import DPassword from "./pages/DomesticTransferPage/DPasswordPage";
import DSuccessPage from "./pages/DomesticTransferPage/DTransferPage.DsuccessPage";
import PersonalAccountDetailPage from "./pages/TravelAccount/page/PersonalAccountDetailPage";

function App() {
  return (
    <div className="App">
      {/* Routes로 Route들을 감싸줌 */}
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/join" element={<JoinPage />} />
        <Route path="/mailInfo" element={<MailInfo />} />
        <Route
          path="/accounts/travel/domestic/:accountId/invite"
          element={<InvitePage />}
        />
        <Route path="/chatbot" element={<ChatbotPage />} />
        <Route path="/chatbot/map" element={<ChatMapPage />} />

        {/* ProtectedRoute로 보호되는 라우트 */}
        <Route element={<ProtectedRoute />}>
          <Route path="/" element={<MainPage />} />
          <Route
            path="/accounts/travel/domestic/:accountId"
            element={<DomesticTravelAccountPage />}
          />
          <Route
            path="/accounts/travel/domestic/:accountId/detail/transfer"
            element={<DTransferLists />}
          />
          <Route
            path="/accounts/travel/domestic/:accountId/detail/transfer/password"
            element={<DPassword />}
          />
          <Route
            path="/accounts/personal/:accountId/detail"
            element={<PersonalAccountDetailPage />}
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
          <Route path="/payment/qr/:accountId" element={<PaymentPage />} />
          <Route
            path="/payment/qr/:accountId/:merchantId/:merchantName/:currency/:amount"
            element={<PaymentPassword />}
          />
          <Route
            path="/payment/qr/success/:payId"
            element={<PaymentSuccessPage />}
          />
          <Route path="/payment/list/:accountId" element={<PaymentHistory />} />
          <Route path="/transfer/list/:accountId" element={<TransferLists />} />
          <Route
            path="/accounts/travel/domestic/:accountId/detail/transfer/:targetaccount"
            element={<DTransferList />}
          />
          <Route
            path="/transfer/list/:accountId/:targetaccount"
            element={<TransferList />}
          />
          <Route
            path="/creation/*"
            element={
              <AccountTypeProvider>
                <Routes>
                  <Route path="/" element={<CreationMainPage />} />
                  <Route path="/bank" element={<BankSelectionPage />} />
                  <Route path="/account" element={<AccountAuthPage />} />
                  <Route path="/identity" element={<IdentityAuthPage />} />
                  <Route path="/setup" element={<AccountSetupPage />} />
                  <Route path="/currency" element={<CurrencyAddPage />} />
                </Routes>
              </AccountTypeProvider>
            }
          />
          <Route path="/chatbot/map" element={<MapTestPage />} />
          <Route path="/exchange" element={<ExchangeRates />} />
          {/* <Route path="/travel/split" element={<ExchangeSplit />} /> */}
          {/* <Route path="/transfer/list" element={<TransferList />} /> */}
          <Route
            path="/transfer/password/:accountId"
            element={<PasswordPage />}
          />
          <Route path="/transfer/success" element={<SuccessPage />} />
          <Route path="/transfer/success/domestic" element={<DSuccessPage />} />
          {/* <Route path="/notification" element={<Notification />} /> */}
        </Route>
      </Routes>

      <BottomBar />
    </div>
  );
}

export default App;
