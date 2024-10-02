import React, { useEffect, useState } from "react";
import axios from "axios";

import bean from "../../assets/bean.png";
import NavBar from "./NavBar";
import ChatBot from "./ChatBot";
import ChangeTargetAmount from "./ChangeTargetAmount";
import Loading from "./Loading";
import client from "../../client";

interface Account {
  accountId: number;
  country: string;
  accountBalance: number;
  exchangeCurrency: string;
}

interface AccountData {
  accountName: string;
  account: Account[];
}

interface Member {
  userId: number;
  userName: string;
  role: string;
}

interface MemberData {
  memberCount: number;
  members: Member[];
}

const TravelAccountPage: React.FC = () => {
  const [loading1, setLoading1] = useState(true);
  const [loading2, setLoading2] = useState(true);
  const [accountData, setAccountData] = useState<AccountData>();
  const [memberData, setMemberData] = useState<MemberData>();

  useEffect(() => {
    axios
      .get("https://j11a604.p.ssafy.io/api/travel/59")
      .then((response) => {
        setAccountData(response.data);
      })
      .catch((error) => {
        console.log(error);
      })
      .finally(() => {
        setLoading1(false);
      });
  }, []);

  useEffect(() => {
    axios
      .get(
        "https://j11a604.p.ssafy.io/api/accounts/travel/domestic/59/members",
        {
          headers: {
            userId: 14,
            userKey: "0996ac35-4916-454e-8101-4560f4f7a0c2",
          },
        },
      )
      .then((response) => {
        setMemberData(response.data);
      })
      .catch((error) => {
        console.log(error);
      })
      .finally(() => {
        setLoading2(false);
      });
  }, []);

  useEffect(() => {
    const requestBody = {
      accountId: 59,
    };

    axios
      .post(
        "https://j11a604.p.ssafy.io/api/accounts/internal/get-accountNo",
        requestBody,
      )
      .then((response) => {
        console.log(response.data);
      })
      .catch((error) => {
        console.log(error);
      })
      .finally(() => {
        console.log("됐다");
      });
  }, []);

  useEffect(() => {
    const response = client("").get("/api/accounts/internal/get-accountNo");
    console.log(response);
  }, []);

  if (loading1) {
    <Loading />;
  }

  if (loading2) {
    <Loading />;
  }

  return (
    <div className="h-full bg-zinc-100">
      {/* 네비게이션 바 */}
      <div className="p-4">
        <NavBar text="Trabean" />
      </div>

      {/* 알림 */}
      <div className="p-4">
        <div className="p-4 bg-red-500">
          <div className="text-right">알림</div>
        </div>
      </div>

      {/* 여행통장 목록 */}
      <div className="p-4">
        <div className="rounded-2xl p-4 bg-red-500">
          <div className="flex">
            <div className="flex-grow text-center">
              {accountData?.accountName}
            </div>
            <div>🖋</div>
          </div>
          <div>
            {accountData?.account.map((account) => (
              <div
                key={account.accountId}
                className="flex justify-between items-center p-4"
              >
                <img className="w-4 h-4" src={bean} alt="이미지" />
                <div className="flex-grow">{account.country}</div>
                <div>
                  {account.exchangeCurrency} {account.accountBalance}
                </div>
              </div>
            ))}
          </div>

          <div className="text-center rounded-3xl bg-green-500 p-2">
            외화 추가하기
          </div>
        </div>
      </div>

      {/* 여행통장 멤버 목록 */}
      <div className="p-4">
        <div className="rounded-2xl p-4 bg-red-500">
          <div className="text-right text-xs">목표관리 멤버관리</div>
          <div className="bg-green-500 my-4">.</div>
          <div className="flex justify-between">
            {memberData?.members.map((member) => (
              <div
                key={member.userId}
                className="flex flex-col items-center m-4"
              >
                <img className="w-10 h-10" src={bean} alt="이미지" />
                <div className="text-xs">{member.userId}</div>
                <div className="text-xs">{member.userName}</div>
                <div className="text-xs">{member.role}</div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* 여행통장 기능 목록 */}
      <div className="p-4">
        <div className="flex justify-between rounded-2xl p-4 bg-red-500">
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
