import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import client from "../../../client";
import { TravelAccountData } from "../type/type";
import { allCurrencies, getCurrencyImage } from "../util/util";
import TopBar from "../../../components/TopBar";
import Loading from "../component/Loading";

const CreateForeignTravelAccountPage: React.FC = () => {
  const { parentAccountId } = useParams();

  const nav = useNavigate();

  const [loading, setLoading] = useState(true); // 서버에서 데이터 수신 여부 체크

  const [selectedCountry, setSelectedCountry] = useState<string | null>(null); // 선택된 국가
  const [selectedCurrency, setSelectedCurrency] = useState<string | null>(null); // 선택된 통화
  const [travelAccountData, setTravelAccountData] =
    useState<TravelAccountData>(); // 한화 여행통장 + 외화 여행통장 상태관리

  // 보유하지 않은 통화 필터링
  const ownedCurrencies = travelAccountData?.account.map(
    (account) => account.exchangeCurrency,
  );
  const availableCurrencies = allCurrencies.filter(
    (currencyObj) => !ownedCurrencies?.includes(currencyObj.exchangeCurrency),
  );

  // Travel 서버 여행통장 조회 API fetch 요청
  useEffect(() => {
    const fetchTravelAccountData = async () => {
      try {
        const response = await client().get(`/api/travel/${parentAccountId}`);
        setTravelAccountData(response.data);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    };

    if (parentAccountId) {
      fetchTravelAccountData();
    }
  }, [parentAccountId]);

  // 로딩 중이면 로딩 스피너 표시
  if (loading) {
    return <Loading />;
  }

  return (
    <div className="h-full relative">
      {/* 네비게이션 바 */}
      <div className="pt-24">
        <TopBar page="환전 통장 개설" isWhite isLogo={false} />
      </div>

      <div className="px-4 py-2 text-center">화폐를 선택해주세요.</div>

      {/* 여회 여행통장 목록 */}
      <div className="px-4 py-2">
        {availableCurrencies.map(({ exchangeCurrency, country }) => (
          <div key={exchangeCurrency}>
            <button
              type="button"
              onClick={() => {
                setSelectedCurrency(exchangeCurrency);
                setSelectedCountry(country);
              }}
              className={`flex justify-between items-center w-full p-4 ${
                selectedCurrency === exchangeCurrency ? "bg-primary" : ""
              }`}
            >
              <div className="flex items-center">
                <img
                  src={getCurrencyImage(exchangeCurrency)}
                  alt={exchangeCurrency}
                  className="w-6 h-6"
                />
                <div className="ml-4">
                  {country} ({exchangeCurrency})
                </div>
              </div>
            </button>
          </div>
        ))}
      </div>

      {/* 다음 버튼 */}
      <div className="absolute bottom-0 w-full px-4 py-24 bg-white">
        <button
          type="button"
          onClick={() =>
            nav(
              `/accounts/travel/foreign/${parentAccountId}/charge?create=true&country=${selectedCountry}&exchangeCurrency=${selectedCurrency}`,
            )
          }
          className={`w-full ${selectedCurrency ? "btn-lg" : "btn-gray-lg"}`}
          disabled={!selectedCurrency}
        >
          다음
        </button>
      </div>
    </div>
  );
};

export default CreateForeignTravelAccountPage;
