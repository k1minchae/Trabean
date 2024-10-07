import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import client from "../../client";
// import NavBar from "./NavBar";
import TopBar from "../../components/TopBar";
import NextStepButton from "./NextStepButton";
import Modal from "./Modal";

interface SubMessage {
  key: string | number;
  text: string;
  className?: string;
}

const AccountVerificationPage: React.FC = () => {
  const query = new URLSearchParams(useLocation().search);
  const bankName = query.get("bank") || "은행";
  const [accountNumber, setAccountNumber] = useState<string>("");
  const [verificationCode, setVerificationCode] = useState<string>("");
  const [step, setStep] = useState<number>(1); // 현재 단계 관리
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const [modalMessage, setModalMessage] = useState<string>("");
  const [subMessage, setSubMessage] = useState<SubMessage[]>([]);
  const [attemptCount, setAttemptCount] = useState(0); // 현재 시도 횟수 상태
  const maxAttempts = 5; // 최대 시도 횟수
  const navigate = useNavigate(); // 화면 이동을 위한 네비게이션 hook

  useEffect(() => {
    if (attemptCount >= maxAttempts) {
      setModalMessage("인증 가능 횟수를 초과하였습니다.");
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
        navigate("/");
      }, 3000);
    }
  }, [attemptCount, maxAttempts, navigate]);

  useEffect(() => {
    const storedIsVerified = sessionStorage.getItem("isSelectedBank");
    if (!storedIsVerified || storedIsVerified !== "true") {
      // 은행이 선택되지 않았으면 접근을 차단하고 리다이렉트
      navigate("/creation");
    }
  }, [navigate]);

  // 계좌번호 입력 핸들러
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setAccountNumber(e.target.value);
  };

  // 인증번호 입력 핸들러
  const handleCodeChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setVerificationCode(e.target.value);
  };

  // 계좌번호 유효성 검사
  const validateAccountNumber = (number: string) => {
    const regex = /^\d{10,16}$/; // 10자리 이상 16자리 이하의 숫자
    return regex.test(number);
  };

  // 1원 인증
  const isVerificationCodeValid = async () => {
    // API 요청 데이터 설정
    const body = {
      accountNo: accountNumber, // 사용자가 입력한 계좌번호
      otp: verificationCode, // 사용자가 입력한 4자리 인증번호
    };

    try {
      const response = await client().post(
        "/api/accounts/verification/onewon", // API 엔드포인트 URL
        body, // 요청 본문 (Axios가 자동으로 JSON으로 변환)
      );

      if (response.status === 200) {
        // 인증번호 일치
        return 1;
      }
      console.log(response.data.responseMessage);
      return 2; // 인증번호 불일치
    } catch (error: unknown) {
      console.error("1원인증 - 서버 오류 발생:", error);
      let errorMessage = "";
      if (error instanceof Error) {
        errorMessage = error.message;
      } else {
        errorMessage = "알 수 없는 문제가 발생했습니다.";
      }

      setModalMessage("서버에 문제가 발생했습니다");
      setSubMessage([
        {
          key: 1,
          text: errorMessage,
          className: "text-xs text-gray-500",
        },
      ]);
      setIsModalOpen(true);
      setTimeout(() => {
        setIsModalOpen(false);
        navigate("/creation");
      }, 2000);
      return 3; // 오류 발생 시 false 반환
    }
  };

  // 1원 송금
  const sendVerifyNumber = async () => {
    try {
      const response = await client().post(
        "/api/accounts/verification/account", // API 엔드포인트 URL
        {
          accountNo: accountNumber, // 요청 본문
        },
      );

      if (response.status === 200) {
        setStep(3);
        setTimeout(() => {
          setStep(4); // 2초 후 인증번호 입력 화면으로 이동
        }, 2000);
      } else {
        setModalMessage(response.data.responseMessage);
        setSubMessage([
          {
            key: 1,
            text: "다시 시도해주세요.",
            className: "text-xs text-gray-500",
          },
        ]);
        setIsModalOpen(true);
      }
    } catch (error: unknown) {
      console.error("1원송금 - 서버 오류 발생:", error);
      let errorMessage = "";
      if (error instanceof Error) {
        errorMessage = error.message;
      } else {
        errorMessage = "알 수 없는 문제가 발생했습니다.";
      }

      setModalMessage("서버에 문제가 발생했습니다");
      setSubMessage([
        {
          key: 1,
          text: errorMessage,
          className: "text-xs text-gray-500",
        },
      ]);
      setIsModalOpen(true);
      setTimeout(() => {
        setIsModalOpen(false);
        navigate("/creation");
      }, 2000);
    }
  };

  // 단계별 다음 버튼 핸들러
  const handleNextStep = async () => {
    switch (step) {
      case 1: // 계좌 입력 페이지
        console.log("step : ", step);
        if (!validateAccountNumber(accountNumber)) {
          setModalMessage("유효하지 않은 계좌번호입니다.");
          setSubMessage([
            {
              key: 1,
              text: "계좌번호를 다시 확인해주세요.",
              className: "text-xs text-gray-500",
            },
          ]);
          setIsModalOpen(true);
        } else {
          setStep(2);
          await sendVerifyNumber();
        }
        break;
      case 2: // 1원 송금 로딩 페이지
        break;
      case 3: // 1원 송금 완료 페이지
        break;
      case 4: // 인증번호 입력 페이지
        if (verificationCode.length === 4) {
          const isValid = await isVerificationCodeValid();
          if (isValid === 1) {
            sessionStorage.setItem("isVerified", "true");
            navigate("/creation/identity");
          } else if (isValid === 2) {
            // 시도 횟수 증가 및 상태 업데이트
            setAttemptCount((prev) => prev + 1);
            if (attemptCount + 1 < maxAttempts) {
              setModalMessage("올바르지 않은 인증번호입니다.");
              setSubMessage([
                {
                  key: 1,
                  text: "입금 내역을 다시 확인해주세요.",
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
          }
        }
        break;
      default:
        break;
    }
  };

  // 모달 닫기 핸들러
  const closeModal = () => {
    setIsModalOpen(false);
  };

  // 단계별 화면 컴포넌트
  const renderStepContent = () => {
    switch (step) {
      case 1:
        return (
          <div className="mb-10">
            <h1 className="text-xl font-bold mt-10">{bankName}은행</h1>
            <p>계좌번호를 입력하세요</p>
            <input
              type="text"
              value={accountNumber}
              onChange={handleInputChange}
              placeholder="계좌번호"
              className="border rounded-md p-2 mt-5 w-full mb-10"
            />
          </div>
        );
      case 2:
        return (
          <div className="text-left mt-10">
            <h1 className="text-lg font-bold mb-4">
              계좌 인증을 위해 <br />
              1원을 보내볼게요
            </h1>
            <p className="mb-4">
              <span className="font-bold">{bankName}은행</span> {accountNumber}
            </p>
            <div className="my-20">
              <div className="circle-loader my-10"> </div>
            </div>
          </div>
        );
      case 3:
        return (
          <div className="text-left mt-10">
            <h1 className="text-lg font-bold mb-4">
              계좌 인증을 위해 <br />
              1원을 보내볼게요
            </h1>
            <p className="mb-4">
              <span className="font-bold">{bankName}은행</span> {accountNumber}
            </p>
            <div className="flex justify-center">
              <div className="check-icon my-14 bg-primary"> </div>
            </div>
          </div>
        );
      case 4:
        return (
          <div className="text-left mt-10">
            <h1 className="text-lg font-bold mb-4">
              우리은행 입금내역을 확인하고 <br />
              ‘Trabean’ 뒤 4자리 숫자를 입력해주세요
            </h1>
            <p className="mb-4">
              {bankName} {accountNumber}
            </p>
            <input
              type="text"
              value={verificationCode}
              onChange={handleCodeChange}
              placeholder="1234"
              className="border rounded-md p-2 w-full mb-10"
            />
          </div>
        );
      default:
        return null;
    }
  };

  return (
    <div>
      <TopBar isLogo={false} page="통장 개설" isWhite />
      <div className="px-6 py-16 bg-white">
        {renderStepContent()}

        <div className="flex justify-center mt-10">
          {(step === 1 || step === 4) && (
            <NextStepButton
              isEnabled={
                (step === 1 && accountNumber.length > 0) ||
                (step === 4 && verificationCode.length === 4)
              }
              onClick={handleNextStep}
              text="다음 단계"
            />
          )}
        </div>

        {isModalOpen && (
          <Modal
            message={modalMessage}
            subMessage={subMessage}
            onClose={closeModal}
          />
        )}

        {/* 로딩 애니메이션 및 체크 아이콘을 위한 스타일 */}
        <style>{`
      .circle-loader {
          border: 20px solid #e5e7eb;
          border-radius: 50%;
          border-top: 20px solid #4caf50;
          width: 150px;
          height: 150px;
          animation: spin 1s linear infinite;
          margin: 0 auto;
        }
        @keyframes spin {
          0% { transform: rotate(0deg); }
          100% { transform: rotate(360deg); }
        }
        .check-icon {
          width: 150px;
          height: 150px;
          border-radius: 50%;
          display: flex;
          position: relative;
        }
        .check-icon::after {
          content: '';
          position: absolute;
          width: 40px;
          height: 80px;
          border: solid white;
          border-width: 0 13px 13px 0;
          transform: rotate(45deg);
          position: absolute;
          top: 50%; 
          left: 50%;
          transform: translate(-50%, -60%) rotate(45deg); 
        }
      `}</style>
      </div>
    </div>
  );
};

export default AccountVerificationPage;
