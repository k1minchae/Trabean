import React from "react";
import { useLocation, useNavigate } from "react-router-dom";

const NavBar: React.FC = () => {
  const location = useLocation();
  const navigate = useNavigate();

  // 현재 경로로부터 페이지 이름 추출 (단순히 경로명을 표시할 수 있음)
  const currentPageName = location.pathname.split("/").pop() || "";

  // 경로와 페이지 명 매핑
  // 네비게이션 바 넣을 때 본인이 알아서 매핑 해주세요
  const pageNames: { [key: string]: string } = {
    "": "Home",
    qr: "QR 결제",
    list: "가계부",
  };

  return (
    <div className="w-[360px] h-[52px] flex items-center fixed top-0 bg-transparent">
      <p className="text-lg px-[10px] relative text-center w-full font-semibold">
        {/* 뒤로가기 버튼 */}
        <span
          className="absolute left-3 cursor-pointer"
          onClick={() => navigate(-1)}
          role="presentation"
        >
          ←
        </span>
        {/* 현재 페이지 정보 */}
        {pageNames[currentPageName] || "페이지"}
      </p>
    </div>
  );
};

export default NavBar;
