// 삭제 예정

import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import TopBar from "../../components/TopBar";
import NextStepButton from "./NextStepButton";
import Keypad from "./Keypad"; // 비밀번호 입력 창 컴포넌트
import Modal from "./Modal";

// 국기 이미지 파일 import
import usdFlag from "../../assets/flagIcon/usd.png";
import eurFlag from "../../assets/flagIcon/eur.png";
import gbpFlag from "../../assets/flagIcon/gbp.png";
import cadFlag from "../../assets/flagIcon/cad.png";
import chfFlag from "../../assets/flagIcon/chf.png";
import cnyFlag from "../../assets/flagIcon/cny.png";
import jpyFlag from "../../assets/flagIcon/jpy.png";
import client from "../../client";

interface SubMessage {
  key: string | number;
  text: string;
  className?: string;
}

const CurrencyAddPage: React.FC = () => {
  const navigate = useNavigate(); // 페이지 이동을 위한 navigate 훅
  const [selectedCurrency, setSelectedCurrency] = useState<string | null>(null); // 선택된 통화 상태
  const [isPasswordStep, setIsPasswordStep] = useState<boolean>(false); // 현재 단계 상태 관리
  const [password, setPassword] = useState<string>(""); // 비밀번호 상태
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const [modalMessage, setModalMessage] = useState<string>("");
  const [subMessage, setSubMessage] = useState<SubMessage[]>([]);
  const [attemptCount, setAttemptCount] = useState(0); // 현재 시도 횟수 상태
  const maxAttempts = 5; // 최대 시도 횟수

  // 통화 목록 데이터
  const currencyList = [
    { name: "미국 달러 USD", value: "USD", img: usdFlag },
    { name: "유럽 유로 EUR", value: "EUR", img: eurFlag },
    { name: "영국 파운드 GBP", value: "GBP", img: gbpFlag },
    { name: "캐나다 달러 CAD", value: "CAD", img: cadFlag },
    { name: "스위스 프랑 CHF", value: "CHF", img: chfFlag },
    { name: "중국 위안화 CNY", value: "CNY", img: cnyFlag },
    { name: "일본 엔화 JPY", value: "JPY", img: jpyFlag },
  ];

  // 통화 항목 클릭 핸들러
  const handleCurrencySelect = (value: string) => {
    setSelectedCurrency(value);
  };

  // 다음 단계 이동 핸들러
  const handleNextStep = () => {
    if (selectedCurrency) {
      setIsPasswordStep(true); // 비밀번호 단계로 변경
    }
  };

  // 비밀번호 검증
  const isVerificationCodeValid = async () => {
    const body = JSON.stringify({
      accountId: 1234,
      password,
    });

    try {
      const response = await client().post(
        "/api/accounts/internal/verify-password",
        body,
      );

      return response.status === 200; // 응답이 성공적인 경우 true 반환
    } catch (error) {
      console.error("인증번호 검증 요청 실패:", error);
      // return false; // 요청 실패 시 false 반환
      return true;
    }
  };

  // 비밀번호 입력 완료 핸들러
  const handlePasswordComplete = async () => {
    // 선택된 통화와 비밀번호 정보를 함께 다음 페이지로 이동
    const isValid = await isVerificationCodeValid();
    if (isValid) {
      sessionStorage.setItem("isVerified", "true");
      navigate("/accounts/travel/domestic");
    } else {
      // 시도 횟수 증가 및 상태 업데이트
      setAttemptCount((prev) => prev + 1);
      if (attemptCount + 1 < maxAttempts) {
        setModalMessage("올바르지 않은 비밀번호입니다.");
        setSubMessage([
          {
            key: 1,
            text: "비밀번호를 다시 확인해주세요.",
            className: "text-xs text-gray-500",
          },
          {
            key: 2,
            text: `남은 시도 횟수 (${attemptCount + 1}/${maxAttempts})`,
            className: "text-xs text-red-500",
          },
        ]);
      }
      setIsModalOpen(true);
      setPassword(""); // 비밀번호 초기화
    }
  };

  // 번호 입력시 password 번호 갱신
  const handlePasswordChange = (newPassword: string) => {
    setPassword(newPassword);
  };

  useEffect(() => {
    if (attemptCount >= maxAttempts) {
      setModalMessage("입력 가능 횟수를 초과하였습니다.");
      setSubMessage([
        {
          key: 1,
          text: "잠시 후 다시 시도해주세요.",
          className: "text-xs text-red-500",
        },
      ]);
      setIsModalOpen(true);

      // 3초 후에 다른 페이지로 이동
      setTimeout(() => {
        navigate("/accounts/travel/domestic"); // 이동할 페이지 경로 설정
      }, 3000);
    }
  }, [attemptCount, maxAttempts, navigate]);

  // 모달 닫기 핸들러
  const closeModal = () => {
    setIsModalOpen(false);
  };

  return (
    <div>
      <TopBar isLogo={false} page="외화 추가" isWhite={isPasswordStep} />
      {!isPasswordStep && (
        <div className="w-full mx-auto px-6 py-8 bg-[#F4F4F5] min-h-screen">
          {/* 상단 텍스트 */}
          <div className="text-left mt-10 mb-6">
            <h1 className="text-md font-semibold mb-2">
              추가하고자 하는 통화를 선택해주세요
            </h1>
            <p className="text-xs text-gray-500">
              * 이미 등록된 통화는 중복으로 추가할 수 없어요
            </p>
          </div>

          {/* 통화 목록 */}
          <div className="flex flex-col space-y-3">
            {currencyList.map((currency) => (
              <button
                type="button"
                key={currency.value}
                onClick={() => handleCurrencySelect(currency.value)}
                className={`flex items-center justify-between p-2 bg-white rounded-lg border ${
                  selectedCurrency === currency.value
                    ? "border-primary"
                    : "border-transparent"
                }`}
              >
                <div className="flex items-center">
                  <img
                    src={currency.img}
                    alt={currency.name}
                    className="w-8 h-8 mr-4 rounded-full"
                  />
                  <span className="text-xs font-medium">{currency.name}</span>
                </div>
                {selectedCurrency === currency.value && (
                  <span className="text-primary font-bold">✔️</span>
                )}
              </button>
            ))}
          </div>

          {/* 다음 단계 버튼 */}
          <div className="flex justify-center mt-10">
            <NextStepButton
              isEnabled={!!selectedCurrency}
              onClick={handleNextStep}
              text="다음 단계"
            />
          </div>
        </div>
      )}

      {isPasswordStep && (
        <div className="w-full mx-auto px-6 py-8 min-h-screen mt-16">
          <p className="text-md mb-4 text-black text-center font-semibold">
            통장 비밀번호를 입력해주세요
          </p>
          <p className="text-gray-700 mb-6 text-sm text-center">
            “모임 통장 비밀 번호 6자리를 입력해주세요”
          </p>
          <Keypad
            password={password}
            onChange={handlePasswordChange}
            onComplete={handlePasswordComplete}
          />
        </div>
      )}

      {isModalOpen && (
        <Modal
          message={modalMessage}
          subMessage={subMessage}
          onClose={closeModal}
        />
      )}
    </div>
  );
};

export default CurrencyAddPage;
