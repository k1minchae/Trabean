import React from "react";

const ChangeTargetAmount: React.FC = () => {
  console.log("목표 금액 수정 모달");
  return (
    <div>
      <div className="bg-blue-500">
        <div>총 얼마를 모을까요?</div>
        <div>금액</div>
        <input />
      </div>
    </div>
  );
};

export default ChangeTargetAmount;
