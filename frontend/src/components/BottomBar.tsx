import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import classNames from "classnames";

import NotificationModal from "../pages/NotificationPage/NotificationPage";

import client from "../client";
import Home from "../assets/home.png";
import PaymentHistory from "../assets/paymentHistory.png";
import Payment from "../assets/payment.png";
import ExchangeRate from "../assets/exchangeRate.png";
import Bell from "../assets/Bell.png";

const BottomBar: React.FC = () => {
  const [notiHidden, setNotiHidden] = useState<boolean>(true);
  const [paymentAccountId, setPaymentAccountId] = useState<number | null>(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const location = useLocation();

  const changeNotiHidden = (hidden: boolean) => {
    setNotiHidden(!hidden);
  };

  // 메인 결제 계좌 가져오기
  const getMainPaymentAccount = async () => {
    try {
      const response = await client().get("/api/payments/main-account");
      setPaymentAccountId(response.data.paymentAccountId);
      setLoading(false);
    } catch (error) {
      setLoading(false);
      console.error("main payment account 불러올 때 에러 발생:", error);
    }
  };

  useEffect(() => {
    if (loading) {
      return;
    }
    getMainPaymentAccount();
  }, [loading]);

  // 결제 페이지 이동
  const goToPaymentPage = () => {
    if (loading) {
      return;
    }
    if (!loading && !paymentAccountId) {
      alert("메인 결제 계좌가 설정되지 않았습니다.");
    } else {
      navigate(`/payment/qr/${paymentAccountId}`);
    }
  };

  // 가계부 페이지 이동
  const goToPaymentListPage = () => {
    if (!paymentAccountId) {
      alert("메인 결제 계좌가 설정되지 않았습니다.");
    }
    navigate(`/payment/list/${paymentAccountId}`);
  };

  // 클래스 이름 설정
  const containerClass = classNames(
    "flex flex-col items-center justify-center relative w-full hover:cursor-pointer",
  );
  const imgClass = classNames("w-[25px] absolute -bottom-1");
  const textClass = classNames("text-xs absolute -bottom-6");
  const isShowBottom =
    !location.pathname.startsWith("/payment/qr") &&
    !location.pathname.startsWith("/login") &&
    !location.pathname.startsWith("/join");

  return (
    <>
      {isShowBottom && (
        <>
          <NotificationModal
            hidden={notiHidden}
            changeNotiHidden={changeNotiHidden}
          />
          <div className="pt-[60px]" />
          <div className="w-[360px] h-[60px] flex items-center fixed bottom-0 text-[#999999] shadow-2xl justify-around bg-white">
            <div
              onClick={() => {
                navigate("/");
              }}
              role="presentation"
              className={containerClass}
            >
              <img src={Home} alt="home" className={imgClass} />
              <p className={textClass}>홈</p>
            </div>
            <div
              onClick={goToPaymentListPage}
              role="presentation"
              className={containerClass}
            >
              <img
                src={PaymentHistory}
                alt="PaymentHistory"
                className={imgClass}
              />
              <p className={textClass}>가계부</p>
            </div>
            <div
              onClick={() => {
                navigate("/exchange");
              }}
              role="presentation"
              className={containerClass}
            >
              <img src={ExchangeRate} alt="exchangeRate" className={imgClass} />
              <p className={textClass}>환율</p>
            </div>
            <div
              onClick={goToPaymentPage}
              role="presentation"
              className={containerClass}
            >
              <img src={Payment} alt="Payment" className={imgClass} />
              <p className={textClass}>결제</p>
            </div>
            <div
              onClick={() => changeNotiHidden(notiHidden)}
              role="presentation"
              className={containerClass}
            >
              <img src={Bell} alt="Bell" className={imgClass} />
              <p className={textClass}>알림</p>
            </div>
          </div>
        </>
      )}
      {null}
    </>
  );
};

export default BottomBar;
