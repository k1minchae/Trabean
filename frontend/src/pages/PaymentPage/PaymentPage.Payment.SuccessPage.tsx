import React from "react";
import TopBar from "../../components/TopBar";
import SuccessIcon from "../../assets/successIcon.png";

const SuccessPage: React.FC = () => {
  console.log("성공 페이지");
  return (
    <>
      <TopBar isWhite isLogo={false} page="QR 결제" />
      <div className="flex flex-col items-center justify-center h-dvh">
        <img
          src={SuccessIcon}
          alt="success-icon"
          className="w-[70px] h-[70px]"
        />
        <p className="text-2xl my-[22px] text-gray-700 font-semibold">
          축하합니다 !
        </p>
        <p className="text-lg text-gray-700">결제에 성공하셨습니다.</p>
        <button type="button" className="btn-lg mt-[20px] w-[238px]">
          확인
        </button>
      </div>
    </>
  );
};

export default SuccessPage;
