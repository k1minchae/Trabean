import React from "react";
import { useParams } from "react-router-dom";
import TopBar from "../../components/TopBar";
import FailModal from "./PaymentPage.Password.FailModal";

const Password: React.FC = () => {
  const { payId, merchantName, currency, amount, userId } = useParams();
  console.log(
    `payId: ${payId}, merchantName: ${merchantName}, currency: ${currency}, amount: ${amount}, userId: ${userId}`,
  );

  // 화폐 이름 매핑
  const currencyNameMap: { [key: string]: string } = {
    CAD: "$ ",
    CHF: "₣ ",
    CNY: "¥ ",
    EUR: "€ ",
    GBP: "£ ",
    JPY: "¥ ",
    USD: "$ ",
    KRW: "₩ ",
  };

  return (
    <div className="flex w-full h-dvh flex-col items-center relative">
      <FailModal />
      <TopBar isLogo={false} isWhite page="QR 결제" />
      <p className="text-lg pt-[100px]">{merchantName}</p>
      <h1 className="text-3xl font-semibold">
        {currency && currencyNameMap[currency]} {amount}
      </h1>
      <p className="text-lg mt-[40px]">계좌 비밀번호를 입력하세요.</p>
    </div>
  );
};

export default Password;
