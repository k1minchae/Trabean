import React from "react";

const ChangeTargetAmount: React.FC = () => {
  console.log("목표 금액 수정 모달");
  return (
    <div>
      <div className="rounded-t-3xl p-4 bg-blue-500">
        <div>
          <div className="font-bold">총 얼마를 모을까요?</div>
          <div>금액</div>
        </div>
        <div className="flex">
          <input />
          <div>원</div>
        </div>
        <div className="flex">
          <div className="btn-light-lg">취소</div>
          <div className="btn-lg">확인</div>
        </div>
      </div>
    </div>
  );
};

export default ChangeTargetAmount;
