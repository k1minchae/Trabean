import React, { useEffect, useState } from "react";
import { useLocation, useParams } from "react-router-dom";
import beans from "../../assets/beans.png";
import client from "../../client";
import Loading from "../TravelAccount/component/Loading";

const InvitePage: React.FC = () => {
  const location = useLocation();

  const queryParams = new URLSearchParams(location.search);
  const email = queryParams.get("email");

  const { accountId } = useParams();

  const [loading1, setLoading1] = useState(true);

  const [accountName, setAccountName] = useState();

  const handleAcceptInvitation = () => {
    alert("참여하기!!!!!!");
  };

  // Travel 서버 한화 여행통장 ID로 이름과 목표 금액 반환 API fetch 요청
  useEffect(() => {
    const fetchTravelAccountData = async () => {
      try {
        const response = await client().get(`/api/travel/info/${accountId}`);
        setAccountName(response.data.name);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading1(false);
      }
    };

    if (accountId) {
      fetchTravelAccountData();
    }
  }, [accountId]);

  // 로딩 중이면 로딩 스피너 표시
  if (loading1) {
    return <Loading />;
  }

  return (
    <div className="h-full flex justify-center items-center bg-green-300">
      <div className="text-center p-4">
        <div>{accountName}</div>
        <img src={beans} alt={beans} className="my-4" />
        <button
          type="button"
          onClick={handleAcceptInvitation}
          className="btn-lg"
        >
          참여하기
        </button>
        <div>
          <div>{accountId}</div>
          <div>{email}</div>
        </div>
      </div>
    </div>
  );
};

export default InvitePage;
