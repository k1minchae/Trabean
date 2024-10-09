import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import client from "../../../client";
// import { formatDateString, getBeanImage, getToday } from "../util/util";
import { formatDateString, getToday } from "../util/util";
// import { TravelAccountMemberData } from "../type/type";
import Loading from "../component/Loading";

interface DomesticTravelAccountFilterModalProps {
  accountId: string | undefined;
  onClose: () => void;
}

const PersonalAccountFilterModal: React.FC<
  DomesticTravelAccountFilterModalProps
> = ({ accountId, onClose }) => {
  const nav = useNavigate();

  const [loading1, setLoading1] = useState(true); // 서버에서 데이터 수신 여부 체크
  // const [loading2, setLoading2] = useState(true); // 서버에서 데이터 수신 여부 체크

  const [selectedOption, setSelectedOption] = useState<string>("all"); // 조회 옵션 상태관리
  const [accountCreationDate, setAccountCreationDate] = useState<string>(""); // 한화 여행통장 생성일 상태관리
  const [startDate, setStartDate] = useState<string>(); // 조회 시작일 상태관리
  const [endDate, setEndDate] = useState<string>(getToday()); // 조회 종료일 상태관리

  // const [travelAccountMemberData, setTravelAccountMemberData] =
  //   useState<TravelAccountMemberData>(); // 여행통장 멤버 상태관리

  const handleStartDateChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setStartDate(e.target.value); // 조회 시작일 상태 업데이트
  };

  const handleEndDateChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEndDate(e.target.value); // 조회 종료일 상태 업데이트
  };

  // 전체기간 버튼 클릭 시 시작일과 종료일 설정
  const handleSelectAllPeriod = () => {
    setStartDate(accountCreationDate); // 계좌 생성일을 시작일로 설정
    setEndDate(getToday()); // 현재 날짜를 종료일로 설정
    setSelectedOption("all");
  };

  // 지정기간 버튼 클릭 시
  const handleSelectCustomPeriod = () => {
    setSelectedOption("custom");
  };

  // 옵션 선택하면 쿼리 파라미터에 담아서 라우팅
  const handleFilterOption = () => {
    const formattedStartDate = startDate?.replace(/-/g, "");
    const formattedEndDate = endDate?.replace(/-/g, "");

    onClose();
    nav(
      `/accounts/personal/${accountId}/detail?startDate=${formattedStartDate}&endDate=${formattedEndDate}`,
    );
  };

  // Account 서버 한화 여행통장 생성일 조회 API fetch 요청
  useEffect(() => {
    const fetchTravelAccountData = async () => {
      try {
        const response = await client().get(
          `/api/accounts/personal/${accountId}/created`,
        );
        setStartDate(formatDateString(response.data.accountCreatedDate));
        setAccountCreationDate(
          formatDateString(response.data.accountCreatedDate),
        );
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

  // Account 서버 한화 여행통장 멤버 목록 조회 API fetch 요청
  // useEffect(() => {
  //   const fetchTravelAccountData = async () => {
  //     try {
  //       const response = await client().get(
  //         `/api/accounts/travel/domestic/${accountId}/members`,
  //       );
  //       setTravelAccountMemberData(response.data);
  //     } catch (error) {
  //       console.error(error);
  //     } finally {
  //       setLoading2(false);
  //     }
  //   };

  //   if (accountId) {
  //     fetchTravelAccountData();
  //   }
  // }, [accountId]);

  // 로딩 중이면 로딩 스피너 표시
  // if (loading1 || loading2) {
  //   return <Loading />;
  // }

  if (loading1) {
    return <Loading />;
  }

  return (
    <div className="bg-white p-4 rounded-t-3xl">
      <div className="px-2 py-4">
        <div className="font-bold mb-2">필터</div>
        <div>조회기간</div>
      </div>
      <div className="px-2 py-4">
        <div className="flex mb-4">
          <button
            type="button"
            className={`w-1/2 mr-2 p-2 border ${
              selectedOption === "all" ? "border-green-500" : "border-gray-300"
            }`}
            onClick={handleSelectAllPeriod}
          >
            전체기간
          </button>
          <button
            type="button"
            className={`w-1/2 ml-2 p-2 border ${
              selectedOption === "custom"
                ? "border-green-500"
                : "border-gray-300"
            }`}
            onClick={handleSelectCustomPeriod}
          >
            지정기간
          </button>
        </div>
        <div className="flex mb-4">
          <input
            type="date"
            value={startDate}
            onChange={handleStartDateChange}
            className="border border-gray-300 rounded w-1/2 p-2 mr-2 text-center"
            disabled={selectedOption === "all"}
          />
          <input
            type="date"
            value={endDate}
            onChange={handleEndDateChange}
            className="border border-gray-300 rounded w-1/2 p-2 ml-2 text-center"
            disabled={selectedOption === "all"}
          />
        </div>
      </div>

      {/* <div className="font-bold px-2 py-4">친구선택</div> */}

      {/* 멤버 목록 */}
      {/* <div className="flex flex-wrap justify-center">
        {travelAccountMemberData?.members.map((member) => (
          <button
            type="button"
            onClick={() => handleMemberClick(member.userId)}
            key={member.userId}
            className={`flex flex-col items-center p-2 ${selectedUserId === member.userId ? "border-2 border-green-500" : ""}`}
          >
            <img
              src={getBeanImage(member.role)}
              alt={member.role}
              className="w-10 h-10"
            />
            <div className="text-xs">{member.userName}</div> */}
      {/* <div className="text-xs">
              ₩{(member.amount ?? 0).toLocaleString()}
            </div> */}
      {/* </button>
        ))}
      </div> */}

      {/* 선택 완료 버튼 */}
      <div className="p-4 mb-4">
        <button
          type="button"
          onClick={handleFilterOption}
          className="btn-lg w-full mx-auto block"
        >
          선택 완료
        </button>
      </div>
    </div>
  );
};

export default PersonalAccountFilterModal;
