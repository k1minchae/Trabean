import React, { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import client from "../../../client";
import { ExchangeEstimateData } from "../type/type";
import TopBar from "../../../components/TopBar";
import Loading from "../component/Loading";
import { getMinimumAmount } from "../util/util";

const ChargeForeignTravelAccountPage: React.FC = () => {
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const create = queryParams.get("create");
  const country = queryParams.get("country");
  const exchangeCurrency = queryParams.get("exchangeCurrency") || "";
  const foreignAccountId = queryParams.get("foreignAccountId");
  const { parentAccountId } = useParams();

  const [minimumAmount, setMinimunAmount] = useState<number>(0); // 최소 환전 금액 상태관리
  const [maxAmount, setMaxAmount] = useState<number>(0); // 최대 환전 금액 상태관리

  const nav = useNavigate();

  const [loading, setLoading] = useState(true);

  const [amount, setAmount] = useState<string>(
    String(getMinimumAmount(exchangeCurrency)),
  ); // 외화 금액 상태관리
  const [exchangeEstimateData, setExchangeEstimateData] =
    useState<ExchangeEstimateData>();

  // 외화 여행통장 생성 후 충전
  const handleChargeAccountBalance = () => {
    const fetchChargeAccountBalance = async (
      withdrawalAccountNo: string,
      depositAccountNo: string,
      depositAccountId: string,
    ) => {
      try {
        await client().post("/api/travel/exchange", {
          krwAccountId: parentAccountId,
          withdrawalAccountNo,
          depositAccountNo,
          exchangeCurrency,
          exchangeAmount: amount,
        });
      } catch (error) {
        console.error(error);
      } finally {
        nav(`/accounts/travel/foreign/${depositAccountId}/detail`);
      }
    };

    const fetchCreateForeignAccount = async () => {
      try {
        const response = await client().post("/api/accounts/travel/foreign", {
          domesticAccountId: parentAccountId,
          currency: exchangeCurrency,
        });
        const withdrawalAccountNo = response.data.domesticAccountNo;
        const depositAccountNo = response.data.foreignAccountNo;
        const depositAccountId = response.data.foreignAccountId;
        fetchChargeAccountBalance(
          withdrawalAccountNo,
          depositAccountNo,
          depositAccountId,
        );
      } catch (error) {
        console.error(error);
      }
    };

    const fetchForeignAccount = async () => {
      try {
        const response = await client().get(
          `/api/accounts/travel/foreign/${foreignAccountId}/couple`,
        );
        const withdrawalAccountNo = response.data.domesticAccountNo;
        const depositAccountNo = response.data.foreignAccountNo;
        const depositAccountId = response.data.foreignAccountId;
        fetchChargeAccountBalance(
          withdrawalAccountNo,
          depositAccountNo,
          depositAccountId,
        );
      } catch (error) {
        console.error(error);
      }
    };

    if (create === "true") {
      fetchCreateForeignAccount();
    } else {
      fetchForeignAccount();
    }
  };

  // 콤마가 적용된 숫자 형식으로 변환하는 함수
  const formatWithCommas = (value: number) => value.toLocaleString();

  // 콤마가 포함된 문자열에서 콤마를 제거하는 함수
  const removeCommas = (value: string) => value.replace(/,/g, "");

  // 입력값에서 콤마 제거된 숫자 상태를 관리하는 함수
  const handleAmountChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = e.target;
    const numericValue = removeCommas(value); // 콤마 제거 후 숫자만 남기기

    // 상태 업데이트 (입력 값에 대한 제한 없음)
    if (/^\d*$/.test(numericValue)) {
      setAmount(numericValue);
    }
  }; // 금액 상태 업데이트

  // Account 서버 한화 여행통장 잔액 조회 API fetch 요청
  useEffect(() => {
    const fetchTravelAccounBalancetData = async () => {
      try {
        const response = await client().get(
          `/api/accounts/travel/domestic/${parentAccountId}/accountBalance`,
        );
        console.log(response.data);
        setMaxAmount(Number(response.data.accountBalance));
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    };

    if (parentAccountId) {
      fetchTravelAccounBalancetData();
    }
  }, [parentAccountId]);

  // Travel 서버 환전 예상 금액 조회 API fetch 요청
  useEffect(() => {
    const fetchTravelAccountData = async () => {
      if (maxAmount >= 0 && amount) {
        try {
          const response = await client().post(
            "/api/travel/exchange/estimate",
            {
              currency: "KRW",
              exchangeCurrency,
              amount,
            },
          );

          const estimatedKrwAmount = Number(
            removeCommas(response.data?.currency?.amount || "0"),
          ); // 콤마 제거 후 숫자로 변환

          // 예상 금액이 최대 금액을 초과하면 경고 메시지 표시
          if (estimatedKrwAmount > maxAmount) {
            // alert("환전에 필요한 금액이 한화 여행통장 잔액을 초과합니다.");
            // setAmount(String(getMinimumAmount(exchangeCurrency))); // 입력 값 초기화
          }
          setExchangeEstimateData(response.data); // 예상 금액 저장
          setMinimunAmount(Number(removeCommas(response.data.currency.amount)));
        } catch (error) {
          console.error(error);
        }
      }
    };

    fetchTravelAccountData();
  }, [amount, exchangeCurrency, maxAmount]);

  if (loading) {
    return <Loading />;
  }

  const isButtonActive =
    maxAmount >= minimumAmount &&
    Number(amount) >= Number(getMinimumAmount(exchangeCurrency)) &&
    amount.endsWith("0");

  return (
    <div className="h-full relative">
      {/* 네비게이션 바 */}
      <div className="pt-24">
        <TopBar isWhite isLogo={false} />
      </div>

      <div className="p-4">
        <div className="font-bold">
          {country} {exchangeCurrency}
        </div>
      </div>

      {/* 금액 입력 필드 */}
      <div className="p-4">
        <div className="flex">
          <input
            type="text"
            value={formatWithCommas(Number(amount))}
            onChange={handleAmountChange}
            placeholder={`최대 ${formatWithCommas(maxAmount)} 원까지 입력 가능`}
            className="border-b border-gray-300 w-full focus:outline-none focus:border-green-500"
          />
          <div className="ml-2">{exchangeCurrency}</div>
        </div>
      </div>

      <div className="px-4">
        <div className="font-bold text-sm text-red-400">
          {maxAmount < minimumAmount ? "잔액이 부족합니다." : ""}
        </div>
        <div className="font-bold text-sm text-red-400">
          {Number(amount) < Number(getMinimumAmount(exchangeCurrency))
            ? "최소 환전 금액이 부족합니다."
            : ""}
        </div>
        <div className="font-bold text-sm text-red-400">
          {amount.endsWith("0") ? "" : "10 단위로 환전이 가능합니다."}
        </div>
      </div>

      {/* 예상 소요 금액 렌더링 */}
      <div className="px-4 py-2">
        <div className="font-bold text-sm text-gray-500 mb-2">한국 KRW</div>
        <div className="font-bold text-sm text-gray-500">
          {exchangeEstimateData?.currency?.amount
            ? `${formatWithCommas(Number(removeCommas(exchangeEstimateData.currency.amount)))} 원`
            : ""}
        </div>
      </div>

      {/* 완료 버튼 */}
      <div className="p-4">
        <button
          type="button"
          onClick={handleChargeAccountBalance}
          disabled={!isButtonActive}
          className={`w-full py-2 text-white ${
            isButtonActive ? "btn-lg" : "btn-gray-lg"
          }`}
        >
          완료
        </button>
      </div>
    </div>
  );
};

export default ChargeForeignTravelAccountPage;
