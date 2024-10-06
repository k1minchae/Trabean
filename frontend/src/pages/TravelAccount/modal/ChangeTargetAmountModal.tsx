import React, { useState } from "react";
import client from "../../../client";

interface ChangeTargetAmountModalProps {
  accountId: string | undefined;
  targetAmount: string | undefined;
  onTargetAmountChange: (newAmount: number) => void;
  onClose: () => void;
}

const ChangeTargetAmountModal: React.FC<ChangeTargetAmountModalProps> = ({
  accountId,
  targetAmount,
  onTargetAmountChange,
  onClose,
}) => {
  const [newTargetAmount, setNewTargetAmount] = useState(
    targetAmount?.replace(/,/g, "") || "",
  ); // 변경할 금액 상태관리

  // 입력값에서 콤마 추가된 문자열을 반환하는 함수
  const formatWithCommas = (value: string) => {
    const numericValue = value.replace(/\D/g, ""); // 숫자 이외의 값 제거
    return Number(numericValue).toLocaleString(); // 콤마 추가된 숫자 반환
  };

  // 입력 값이 변경될 때마다 콤마 추가한 값으로 업데이트
  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = e.target;
    setNewTargetAmount(value.replace(/,/g, "")); // 상태에 콤마 없는 값 저장
  };

  // 확인 버튼을 눌렀을 때 서버로 PUT 요청 전송
  const handleUpdateTargetAmount = () => {
    if (newTargetAmount === "" || Number.isNaN(Number(newTargetAmount))) {
      alert("유효한 금액을 입력하세요.");
      return;
    }

    const fetchUpdateTargetAmount = async () => {
      try {
        await client().put("/api/travel/change/targetAmount", {
          accountId,
          targetAmount: newTargetAmount,
        });
        alert("목표 금액이 성공적으로 변경되었습니다!");
        onTargetAmountChange(Number(newTargetAmount));
        onClose();
      } catch (error) {
        alert("목표 금액 변경에 실패했습니다.");
        console.error(error);
      }
    };

    fetchUpdateTargetAmount();
  };

  return (
    <div className="bg-white p-4 rounded-t-3xl">
      <div className="p-4">
        <div className="font-bold mb-4">총 얼마를 모을까요?</div>
        <div>금액</div>
      </div>
      <div className="flex p-4">
        <input
          type="text"
          value={formatWithCommas(newTargetAmount)}
          onChange={handleInputChange}
          min="0"
          className="flex-grow border-b-2 border-gray-300 outline-none mr-2"
        />
        <div>원</div>
      </div>
      <div className="flex justify-between p-4 mb-4">
        <button
          type="button"
          onClick={onClose}
          className="btn-light-lg w-1/2 mr-2"
        >
          취소
        </button>
        <button
          type="button"
          onClick={handleUpdateTargetAmount}
          className="btn-lg w-1/2 ml-2"
        >
          확인
        </button>
      </div>
    </div>
  );
};

export default ChangeTargetAmountModal;
