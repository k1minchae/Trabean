import React from "react";
import TopBar from "../../components/TopBar";

const NavBar: React.FC = () => {
  console.log("결제 비밀번호페이지");
  return (
    <>
      <TopBar isLogo={false} isWhite page="QR 결제" />
      <div className="pt-[80px]">
        <p>비밀번호 페이지</p>
      </div>
    </>
  );
};

export default NavBar;
