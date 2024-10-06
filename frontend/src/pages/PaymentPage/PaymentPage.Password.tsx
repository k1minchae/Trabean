import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

import TopBar from "../../components/TopBar";
import FailModal from "./PaymentPage.Password.FailModal";
import Keypad from "../AccountCreationPage/Keypad";
import client from "../../client";
import useAuthStore from "../../store/useAuthStore";

const Password: React.FC = () => {
  const { merchantId, merchantName, currency, amount } = useParams();
  const [isFail, setIsFail] = useState<boolean>(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [krwAmount, setKrwAmount] = useState<number | null>(null);
  const [foreignAmount, setForeignAmount] = useState<number | null>(null);
  const [password, setPassword] = useState<string>("");
  const [payId, setPayId] = useState<number | null>(null);

  const navigate = useNavigate();
  const validateInfo = () => {
    if (amount === undefined) {
      setErrorMessage("가격 정보를 불러오는 데 실패했습니다.");
      setIsFail(true);
      return;
    }
    if (currency === undefined) {
      setErrorMessage("통화 정보를 불러오는 데 실패했습니다.");
      setIsFail(true);
      return;
    }
    if (merchantId === undefined) {
      setErrorMessage("가맹점 정보를 불러오는 데 실패했습니다.");
      setIsFail(true);
      return;
    }
    if (currency === "KRW") {
      setKrwAmount(parseInt(amount, 10));
    } else {
      setForeignAmount(parseInt(amount, 10));
    }
  };

  const handleModal = () => {
    setIsFail(false);
    setErrorMessage(null);
  };

  // qr 읽어온 직후 정보 업데이트 api 호출
  const updatePaymentInfo = async () => {
    const accountId = useAuthStore.getState().paymentAccountId;
    try {
      validateInfo();
      const requestBody: {
        accountId: string | null;
        merchantId?: string;
        krwAmount?: number;
        foreignAmount?: number;
      } = {
        accountId,
        merchantId,
      };
      if (krwAmount !== null) {
        requestBody.krwAmount = krwAmount;
      } else if (foreignAmount !== null) {
        requestBody.foreignAmount = foreignAmount;
      }
      const response = await client().post(`/api/payments/info`, requestBody);
      setIsFail(false);
      setErrorMessage(null);
      setPayId(response.data.data.payId);
    } catch (error) {
      setIsFail(true);
      if (error instanceof Error) {
        setErrorMessage(error.message || "알 수 없는 에러가 발생했습니다.");
        setIsFail(true);
      }
    }
  };

  useEffect(() => {
    updatePaymentInfo();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // 결제 요청 api 호출
  const requestPayment = async (
    accountId: string | null,
    transactionId: string,
  ) => {
    try {
      // 요청 바디 생성
      const requestBody = {
        transactionId,
        payId,
        merchantId,
        krwAmount: krwAmount ?? null,
        foreignAmount: foreignAmount ?? null,
      };
      // API 요청 전송
      const response = await client().post(
        `/api/payments/${accountId}`,
        requestBody,
      );
      console.log(response.data);
      // 성공시 성공 url 로 이동
      if (response.data.status === "SUCCESS") {
        navigate(`/payment/qr/success/${payId}`);
      }
    } catch (error) {
      if (payId == null) {
        // payId가 null일 때
        setErrorMessage("결제 ID를 가져오는 데 실패했습니다.");
        setIsFail(true);
        return;
      }
      if (error instanceof Error) {
        // 에러 메시지 설정
        setErrorMessage(error.message || "알 수 없는 에러 발생");
        setIsFail(true);
      }
    }
  };

  const changePasswordInput = (newPassword: string) => {
    setPassword(newPassword);
  };

  // 비밀번호 입력 완료
  const submitPassword = async () => {
    try {
      // 필요한 데이터 가져오기
      const accountId = useAuthStore.getState().paymentAccountId;

      // 요청 바디 생성
      const requestBody = {
        payId,
        accountId,
        password,
      };

      // API 요청 전송
      const response = await client().post(
        `/api/payments/validate`,
        requestBody,
      );
      console.log(response.data);

      // 발급된 transactionId 로 결제요청
      requestPayment(accountId, response.data.transactionId);
    } catch (error) {
      if (payId == null) {
        // payId가 null일 때
        setErrorMessage("결제 ID를 가져오는 데 실패했습니다.");
        setIsFail(true);
        return;
      }
      if (error instanceof Error) {
        // 에러 메시지 설정
        setErrorMessage(error.message || "알 수 없는 에러가 발생했습니다.");
        setIsFail(true);
      }
    }
  };

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
      {isFail && <FailModal message={errorMessage} handleModal={handleModal} />}
      <TopBar isLogo={false} isWhite page="QR 결제" />

      <p className="text-lg pt-[100px]">{merchantName}</p>
      <h1 className="text-3xl font-semibold">
        {currency && currencyNameMap[currency]} {amount}
      </h1>
      <p className="text-lg mt-[40px] mb-5">계좌 비밀번호를 입력하세요.</p>
      <Keypad
        password={password}
        onChange={changePasswordInput}
        onComplete={submitPassword}
      />
    </div>
  );
};

export default Password;
