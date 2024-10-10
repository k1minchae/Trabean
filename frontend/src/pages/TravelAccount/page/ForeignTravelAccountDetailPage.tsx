import React, { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import filter from "../../../assets/filter.png";
import client from "../../../client";
import {
  ForeignTravelAccountDetailData,
  ForeignTravelAccountTransaction,
} from "../type/type";
import TopBar from "../../../components/TopBar";
import Loading from "../component/Loading";
import ForeignTravelAccountFilterModal from "../modal/ForeignTravelAccountFilterModal";
import { getCurrencySymbol } from "../util/util";

const ForeignTravelAccountDetailPage: React.FC = () => {
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const startDate = queryParams.get("startDate");
  const endDate = queryParams.get("endDate");
  const { accountId } = useParams(); // Path Variable

  const nav = useNavigate();

  const [userRole, setUserRole] = useState<string>("");

  const [loading1, setLoading1] = useState(true); // 서버에서 데이터 수신 여부 체크
  const [loading2, setLoading2] = useState(true); // 서버에서 데이터 수신 여부 체크

  const [parentAccountId, setParentAccountId] = useState();
  const [foreignTravelAccountDetailData, setForeignTravelAccountDetailData] =
    useState<ForeignTravelAccountDetailData>(); // 한화 여행통장 상세조회 상태관리

  // 검색 필터 모달
  const [isChangeFilterModalOpen, setIsChangeFilterModalOpen] = useState(false);

  const openChangeFilterModal = () => setIsChangeFilterModalOpen(true);
  const closeChangeFilterModal = () => setIsChangeFilterModalOpen(false);

  // Acount 서버 통장 권한 조회 API fetch 요청
  useEffect(() => {
    const fetchUserRole = async () => {
      try {
        const response = await client().get(
          `api/accounts/travel/domestic/${accountId}/userRole`,
        );
        setUserRole(response.data.userRole);
      } catch (error) {
        console.error(error);
      }
    };

    fetchUserRole();
  }, [accountId, userRole]);

  // Travel 서버 외화 여행통장 ID로 한화 여행통장 ID 반환
  useEffect(() => {
    const getDomesticTravelAccountIdData = async () => {
      try {
        const response = await client().get(`/api/travel/parents/${accountId}`);
        setParentAccountId(response.data.parentAccountId);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading1(false);
      }
    };

    if (accountId) {
      getDomesticTravelAccountIdData();
    }
  }, [accountId]);

  // Travel 서버 여행통장(외화) 상세 조회 API fetch 요청
  useEffect(() => {
    const getForeignTravelAccountDetailData = async () => {
      try {
        const response = await client().get(
          `/api/travel/foreign/${accountId}`,
          { params: { startDate, endDate } },
        );
        setForeignTravelAccountDetailData(response.data);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading2(false);
      }
    };

    if (accountId) {
      getForeignTravelAccountDetailData();
    }
  }, [accountId, startDate, endDate]);

  // 입금은 초록색, 출금은 빨간색으로 표시
  const getBalanceColor = (transactionType: string) => {
    if (transactionType === "1") {
      return "text-green-500"; // 초록색
    }
    if (transactionType === "2") {
      return "text-red-500"; // 빨간색
    }
    return "text-black"; // 기본 검정색
  };

  // 날짜 형식을 변환하는 함수
  const formatDate = (dateString: string): string => {
    const year = dateString.substring(0, 4);
    const month = dateString.substring(4, 6);
    const day = dateString.substring(6, 8);
    return `${year}.${month}.${day}`;
  };

  // 거래 내역을 날짜별로 그룹화하는 함수
  const groupByDate = (
    transactions: ForeignTravelAccountTransaction[],
  ): { [date: string]: ForeignTravelAccountTransaction[] } =>
    transactions.reduce(
      (acc, transaction) => {
        const date = transaction.transactionDate;
        if (!acc[date]) {
          acc[date] = [];
        }
        acc[date].push(transaction);
        return acc;
      },
      {} as { [date: string]: ForeignTravelAccountTransaction[] },
    );

  // 날짜별로 그룹화된 거래 내역을 렌더링하는 함수
  const renderGroupedTransactions = () => {
    if (!foreignTravelAccountDetailData?.list) return null;

    const groupedTransactions = groupByDate(
      foreignTravelAccountDetailData.list,
    );

    // 날짜를 내림차순으로 정렬
    const sortedDates = Object.keys(groupedTransactions).sort(
      (a, b) => Number(b) - Number(a),
    );

    return sortedDates.map((date) => (
      <div key={date}>
        {/* 날짜 헤더 (변환된 날짜 표시) */}
        <div className="font-bold text-sm bg-zinc-100 px-2 my-2">
          {formatDate(date)}
        </div>
        {groupedTransactions[date].map((transaction, index) => (
          <div
            // eslint-disable-next-line react/no-array-index-key
            key={index}
            className="flex justify-between px-2 py-4 border-b border-gray-300"
          >
            <div>
              <div className="font-bold py-1">
                {transaction.transactionSummary}
              </div>
              {transaction.transactionType === "1" ? (
                <div className="text-xs">입금</div>
              ) : (
                <div className="text-xs">출금</div>
              )}
            </div>
            <div className="text-right">
              <div
                className={`py-1 font-bold ${getBalanceColor(transaction.transactionType)}`}
              >
                {transaction.transactionType === "1"
                  ? `+${getCurrencySymbol(foreignTravelAccountDetailData.exchangeCurrency)}${transaction.transactionBalance.toLocaleString()}`
                  : `-${getCurrencySymbol(foreignTravelAccountDetailData.exchangeCurrency)}${transaction.transactionBalance.toLocaleString()}`}
              </div>
              <div className="text-xs">
                {getCurrencySymbol(
                  foreignTravelAccountDetailData.exchangeCurrency,
                )}
                {transaction.transactionAfterBalance.toLocaleString()}
              </div>
            </div>
          </div>
        ))}
      </div>
    ));
  };

  // 로딩 중이면 로딩 스피너 표시
  if (loading1 || loading2) {
    return <Loading />;
  }

  return (
    <div className="h-full relative">
      {/* 네비게이션 바 */}
      <div className="pt-24">
        <TopBar
          page="이용내역"
          isWhite
          isLogo={false}
          path={`/accounts/travel/domestic/${parentAccountId}`}
        />
      </div>

      {/* 외화 여행통장 정보 */}
      <div className="px-4 py-2">
        <div className="text-center">
          <div className="font-bold text-xl p-2">
            {foreignTravelAccountDetailData?.country}{" "}
            {foreignTravelAccountDetailData?.exchangeCurrency}
          </div>
          <div className="font-bold p-2">
            {foreignTravelAccountDetailData ? (
              <>
                {`${getCurrencySymbol(
                  foreignTravelAccountDetailData.exchangeCurrency,
                )}`}
                {foreignTravelAccountDetailData?.accountBalance.toLocaleString(
                  undefined,
                  { minimumFractionDigits: 1, maximumFractionDigits: 1 },
                )}
              </>
            ) : (
              ""
            )}
          </div>
        </div>
        <div className="py-4">
          <button
            type="button"
            onClick={() => {
              nav(
                `/accounts/travel/foreign/${parentAccountId}/charge?create=false&country=${foreignTravelAccountDetailData?.country}&exchangeCurrency=${foreignTravelAccountDetailData?.exchangeCurrency}&foreignAccountId=${accountId}`,
              );
            }}
            className={`w-1/2 mx-auto block ${userRole === "NONE_PAYER" ? "btn-gray-md" : "btn-md"}`}
            disabled={userRole === "NONE_PAYER"}
          >
            충전하기
          </button>
        </div>
      </div>

      <div className="my-2 border-b border-gray-300" />

      <div className="flex justify-between px-2 mb-4">
        <div className="text-sm">전체기간</div>
        <div>
          <button type="button" onClick={openChangeFilterModal}>
            <img src={filter} alt={filter} className="h-4 w-4" />
          </button>
        </div>
      </div>

      {/* 거래 내역 */}
      <div>{renderGroupedTransactions()}</div>

      {/* 필터 모달 */}
      {isChangeFilterModalOpen ? (
        <div className="absolute inset-0 flex items-end py-12 bg-gray-900 bg-opacity-50">
          <div className="w-full">
            <ForeignTravelAccountFilterModal
              accountId={accountId}
              onClose={closeChangeFilterModal}
            />
          </div>
        </div>
      ) : null}
    </div>
  );
};

export default ForeignTravelAccountDetailPage;
