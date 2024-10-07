import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAccountType } from "./AccountTypeContext";
import Keypad from "./Keypad";
import client from "../../client";
// import NavBar from "./NavBar";
import TopBar from "../../components/TopBar";
import Modal from "./Modal";
import SuccessPage from "./SuccessPage";
import NextStepButton from "./NextStepButton";

interface SubMessage {
  key: string | number;
  text: string;
  className?: string;
}

const PasswordInputPage: React.FC = () => {
  const [step, setStep] = useState(1); // 현재 단계 (1: 통장 이름, 2: 목표 금액, 3: 비밀번호 입력, 4: 비밀번호 확인, 5: 성공 화면)
  const [accountName, setAccountName] = useState(""); // 통장 이름 상태
  const [targetAmount, setTargetAmount] = useState(""); // 목표 금액 상태
  const [formattedAmount, setFormattedAmount] = useState("");
  const [password, setPassword] = useState(""); // 비밀번호 상태
  const [confirmPassword, setConfirmPassword] = useState(""); // 비밀번호 확인 상태
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const [modalMessage, setModalMessage] = useState(""); // 모달 메시지 상태
  const [subMessage, setSubMessage] = useState<SubMessage[]>([]);
  const { accountType, resetAccountType } = useAccountType(); // Context 값 가져오기
  const navigate = useNavigate(); // 화면 이동을 위한 네비게이션 hook

  useEffect(() => {
    const storedIsVerified = sessionStorage.getItem("isIdentityAuth");
    if (!storedIsVerified || storedIsVerified !== "true") {
      // 인증되지 않았으면 접근을 차단하고 리다이렉트
      navigate("/creation");
    }
  }, [navigate]);

  // 통장 유형에 알맞는 step 설정
  useEffect(() => {
    if (accountType === "personal") {
      setStep(3);
    }
  }, [accountType]);

  // 통장 이름 입력 핸들러
  const handleAccountNameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setAccountName(e.target.value);
  };

  // 목표 금액 입력 핸들러
  const handleTargetAmountChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const input = e.target.value;
    const numericValue = input.replace(/[^0-9]/g, "");
    const formattedValue = Number(numericValue).toLocaleString();
    setFormattedAmount(formattedValue);
    setTargetAmount(numericValue);
  };

  // 비밀번호 입력 변경 핸들러
  const handlePasswordChange = (newPassword: string) => {
    if (step === 3) {
      setPassword(newPassword);
    } else {
      setConfirmPassword(newPassword);
    }
  };

  // 계좌 개설 요청
  const creationAccount = async () => {
    console.log("target : ", targetAmount);
    try {
      // 요청 URL 및 Body 설정
      let url = "/api/accounts/personal";
      const requestBody: {
        password: string;
        accountName?: string;
        targetAmount?: number;
      } = { password };

      if (accountType === "travel") {
        requestBody.accountName = accountName;
        requestBody.targetAmount = parseInt(targetAmount, 10); // 목표 금액을 정수형으로 변환
        url = "/api/accounts/travel/domestic";
      }

      // 계좌 개설 요청 전송
      await client().post(url, requestBody);

      // 성공 시 다음 단계로 이동
      setStep(5);
    } catch (error) {
      // 에러가 AxiosError 인스턴스인지 확인
      if (error instanceof Error && error.message) {
        setModalMessage(error.message || "통장 개설에 실패했습니다.");
      } else {
        setModalMessage("알 수 없는 오류가 발생했습니다.");
      }

      // 상태 값 설정
      setSubMessage([
        {
          key: 1,
          text: "다시 시도해주세요.",
          className: "text-xs text-gray-500",
        },
      ]);
      setIsModalOpen(true); // 오류 메시지를 모달로 표시
      setStep(3); // 비밀번호 입력 단계로 되돌아가기
      setPassword("");
      setConfirmPassword(""); // 비밀번호 초기화
    }
  };

  // 완료 버튼 핸들러
  const handleComplete = async () => {
    if (step === 3) {
      setStep(4); // 비밀번호 입력 완료 -> 재확인 단계로 이동
    } else if (step === 4 && confirmPassword === password) {
      creationAccount();
    } else {
      setModalMessage("비밀번호가 일치하지 않습니다.");
      setSubMessage([
        {
          key: 1,
          text: "다시 시도해주세요.",
          className: "text-xs text-gray-500",
        },
      ]);
      setIsModalOpen(true);
      setStep(3); // 비밀번호 입력 단계로 되돌아가기
      setPassword("");
      setConfirmPassword(""); // 비밀번호 초기화
    }
  };

  // 다음 단계 이동
  const goToNextStep = () => {
    if (step === 1) {
      if (accountName.length > 15) {
        setIsModalOpen(true);
        setModalMessage("이름이 너무 깁니다.");
        setSubMessage([
          {
            key: 1,
            text: "15글자 이하로 다시 입력해주세요.",
            className: "text-xs text-gray-500",
          },
        ]);
        return;
      }
    } else if (step === 2) {
      if (!/^\d+$/.test(targetAmount)) {
        setIsModalOpen(true);
        setModalMessage("금액은 숫자만 입력 가능합니다.");
        setSubMessage([
          {
            key: 1,
            text: "다시 입력해주세요.",
            className: "text-xs text-gray-500",
          },
        ]);
        return;
      }

      const targetAmountValue = BigInt(targetAmount || "0");
      const longMaxValue = BigInt("9223372036854775807");
      if (targetAmountValue > longMaxValue) {
        setIsModalOpen(true);
        setModalMessage("설정 가능한 금액 한도를 초과했습니다.");
        setSubMessage([
          {
            key: 1,
            text: "금액을 다시 설정해주세요",
            className: "text-xs text-gray-500",
          },
        ]);
        return;
      }
    }
    setStep(step + 1);
  };

  // 모달 닫기 핸들러
  const closeModal = () => {
    setIsModalOpen(false);
  };

  // 개설 완료 버튼 핸들러
  const handleNextStep = () => {
    resetAccountType();
    navigate(`/`);
  };

  const returnPage = () => {
    resetAccountType(); // Context 상태 초기화
    navigate(`/`); // 이전 페이지로 이동
  };

  if (!accountType) {
    return (
      <div className="flex justify-center items-center h-screen">
        <div className="text-left">
          <div className="font-bold text-lg">! 잘못된 접근 입니다.</div>
          <div className="mb-10">통장 유형이 선택되지 않았습니다.</div>
          <div className="flex justify-center">
            <NextStepButton isEnabled onClick={returnPage} text="돌아가기" />
          </div>
        </div>
      </div>
    );
  }

  if (accountType === "personal") {
    return (
      <div>
        <TopBar isLogo={false} page="통장 개설" isWhite />
        <div className="px-6 py-20 bg-white">
          {step === 3 && (
            <>
              <h1 className="text-lg font-semibold mb-1">마지막 단계에요!</h1>
              <p className="text-md mb-8 text-gray-500">
                사용하실 통장 비밀번호를 입력해주세요
              </p>
              <p className="text-gray-500 mb-5 text-sm text-center">
                “총 6자리의 숫자로 구성된 비밀번호를 입력해주세요.”
              </p>
              <Keypad
                password={password}
                onChange={handlePasswordChange}
                onComplete={handleComplete}
              />
            </>
          )}

          {step === 4 && (
            <>
              <h1 className="text-lg font-semibold mb-1">마지막 단계에요!</h1>
              <p className="text-md mb-8 text-gray-500">
                통장 비밀번호를 다시 한 번 입력해주세요
              </p>
              <p className="text-gray-500 mb-5 text-sm text-center">
                “비밀번호를 다시 확인하고 있어요”
              </p>
              <Keypad
                password={confirmPassword}
                onChange={handlePasswordChange}
                onComplete={handleComplete}
              />
            </>
          )}

          {step === 5 && (
            <SuccessPage
              title="축하합니다!"
              message="여행 통장 개설에 성공하셨습니다."
            />
          )}

          {step === 5 && (
            <div className="flex justify-center px-6">
              <NextStepButton isEnabled onClick={handleNextStep} text="확인" />
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
      </div>
    );
  }

  return (
    <div>
      <TopBar isLogo={false} page="통장 개설" isWhite />
      <div className="px-6 py-20 bg-white">
        {step === 1 && (
          <>
            <div className="text-center">
              <p className="text-md mb-8">
                멤버들과 함께 사용할{" "}
                <p>
                  여행 계좌의 <span className="font-bold">별명</span>을
                  지어주세요.
                </p>
              </p>
              <input
                type="text"
                value={accountName}
                onChange={handleAccountNameChange}
                placeholder="최대 15글자까지 입력 가능합니다."
                className="border rounded-md p-2 mt-4 w-4/5 mb-10"
              />
            </div>
            <div className="flex justify-center px-6 mt-6">
              <NextStepButton
                isEnabled={accountName.length > 0}
                onClick={goToNextStep}
                text="다음"
              />
            </div>
          </>
        )}

        {step === 2 && (
          <>
            <div className="text-center">
              <p className="text-md mb-8">
                목표 금액 설정
                <p className="text-md mb-8 text-gray-500">
                  여행 계좌의 <span className="font-bold">목표 금액</span>을
                  설정해주세요.
                </p>
              </p>
              <input
                type="text"
                value={formattedAmount}
                onChange={handleTargetAmountChange}
                placeholder="0"
                className="border rounded-md p-2 mt-4 w-4/5 mb-10"
              />
            </div>

            <div className="flex justify-center px-6 mt-6">
              <NextStepButton
                isEnabled={targetAmount.length > 0}
                onClick={goToNextStep}
                text="다음"
              />
            </div>
          </>
        )}

        {step === 3 && (
          <>
            <h1 className="text-lg font-semibold mb-1">마지막 단계에요!</h1>
            <p className="text-md mb-8 text-gray-500">
              사용하실 통장 비밀번호를 입력해주세요
            </p>
            <p className="text-gray-500 mb-5 text-sm text-center">
              “모두가 함께 공유하는 비밀번호에요”
            </p>
            <Keypad
              password={password}
              onChange={handlePasswordChange}
              onComplete={handleComplete}
            />
          </>
        )}

        {step === 4 && (
          <>
            <h1 className="text-lg font-semibold mb-1">마지막 단계에요!</h1>
            <p className="text-md mb-8 text-gray-500">
              통장 비밀번호를 다시 한 번 입력해주세요
            </p>
            <p className="text-gray-500 mb-5 text-sm text-center">
              “비밀번호를 다시 확인하고 있어요”
            </p>
            <Keypad
              password={confirmPassword}
              onChange={handlePasswordChange}
              onComplete={handleComplete}
            />
          </>
        )}

        {step === 5 && (
          <SuccessPage
            title="축하합니다!"
            message="여행 통장 개설에 성공하셨습니다."
          />
        )}

        {step === 5 && (
          <div className="flex justify-center px-6">
            <NextStepButton isEnabled onClick={handleNextStep} text="확인" />
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
    </div>
  );
};

export default PasswordInputPage;
