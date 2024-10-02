import React, { useEffect, useState } from "react";
import axios from "axios";

import bean from "../../assets/bean.png";
import NavBar from "./NavBar";
import ChatBot from "./ChatBot";
import ChangeTargetAmount from "./ChangeTargetAmount";

const TravelAccountPage: React.FC = () => {
  const [loading, setLoading] = useState(true);

  const [accountName, setAccountName] = useState();
  const [account, setAccount] = useState([]);

  useEffect(() => {
    axios
      .get("http://j11a604.p.ssafy.io/api/travel/58")
      .then((response) => {
        setAccountName(response.data.accountName);
        setAccount(response.data.account);
      })
      .catch((error) => {
        console.log(error);
      })
      .finally(() => {
        setLoading(false);
        console.log(loading);
        console.log(accountName);
        console.log(account);
      });
  }, []);

  return (
    <div className="h-full p-4 bg-zinc-100">
      {/* 네비게이션 바 */}
      <div>
        <NavBar text="Trabean" />
      </div>

      {/* 알림 */}
      <div className="my-4 p-4 bg-red-500">
        <div className="text-right">알림</div>
      </div>

      {/* 여행통장 목록 */}
      <div className="rounded-2xl my-4 p-4 bg-red-500">
        <div className="flex">
          <div className="flex-grow text-center">까비와 함께하는 세계일주</div>
          <div>🖋</div>
        </div>
        <div className="flex justify-between items-center p-4">
          <img className="w-4 h-4" src={bean} alt="이미지" />
          <div className="flex-grow">대한민국</div>
          <div>5,800</div>
        </div>
        <div className="flex justify-between items-center p-4">
          <img className="w-4 h-4" src={bean} alt="이미지" />
          <div className="flex-grow">대한민국</div>
          <div>5,800</div>
        </div>
        <div className="flex justify-between items-center p-4">
          <img className="w-4 h-4" src={bean} alt="이미지" />
          <div className="flex-grow">대한민국</div>
          <div>5,800</div>
        </div>
        <div className="text-center rounded-3xl bg-green-500 p-2">
          외화 추가하기
        </div>
      </div>

      {/* 여행통장 멤버 목록 */}
      <div className="rounded-2xl my-4 p-4 bg-red-500">
        <div className="text-right text-xs">목표관리 멤버관리</div>
        <div className="bg-green-500 my-4">.</div>
        <div className="flex justify-between">
          <div className="flex flex-col items-center">
            <img className="w-10 h-10" src={bean} alt="이미지" />
            <div className="text-xs">김민채</div>
            <div className="text-xs">₩3,721</div>
          </div>
          <div className="flex flex-col items-center">
            <img className="w-10 h-10" src={bean} alt="이미지" />
            <div className="text-xs">김민채</div>
            <div className="text-xs">₩3,721</div>
          </div>
          <div className="flex flex-col items-center">
            <img className="w-10 h-10" src={bean} alt="이미지" />
            <div className="text-xs">김민채</div>
            <div className="text-xs">₩3,721</div>
          </div>
          <div className="flex flex-col items-center">
            <img className="w-10 h-10" src={bean} alt="이미지" />
            <div className="text-xs">김민채</div>
            <div className="text-xs">₩3,721</div>
          </div>
          <div className="flex flex-col items-center">
            <img className="w-10 h-10" src={bean} alt="이미지" />
            <div className="text-xs">김민채</div>
            <div className="text-xs">₩3,721</div>
          </div>
        </div>
      </div>

      {/* 여행통장 기능 목록 */}
      <div className="flex justify-between rounded-2xl my-4 p-4 bg-red-500">
        <div className="flex flex-col items-center bg-white rounded-3xl px-4 py-1">
          <div>
            <img className="w-10 h-10" src={bean} alt="이미지" />
          </div>
          <div className="text-sm">친구들과</div>
          <div className="text-sm font-bold">N빵하기</div>
        </div>
        <div className="flex flex-col items-center bg-white rounded-3xl px-4 py-1">
          <div>
            <img className="w-10 h-10" src={bean} alt="이미지" />
          </div>
          <div className="text-sm">다함께</div>
          <div className="text-sm font-bold">결제해요</div>
        </div>
        <div className="flex flex-col items-center bg-white rounded-3xl px-4 py-1">
          <div>
            <img className="w-10 h-10" src={bean} alt="이미지" />
          </div>
          <div className="text-sm">예산관리</div>
          <div className="text-sm font-bold">가계부</div>
        </div>
      </div>

      {/* 챗봇 */}
      <div className="p-4">
        <ChatBot />
      </div>

      {/* 목표 금액 수정 모달 */}
      <div>
        <ChangeTargetAmount />
      </div>
    </div>
  );
};

export default TravelAccountPage;
