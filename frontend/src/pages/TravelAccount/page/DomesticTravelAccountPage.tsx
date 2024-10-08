import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import settings from "../../../assets/settings.png";
import dollarCoin from "../../../assets/Dollar Coin.png";
import moneyBox from "../../../assets/Money Box.png";
import eventNote from "../../../assets/Event note.png";
import client from "../../../client";
import { TravelAccountData, TravelAccountMemberAmountData } from "../type/type";
import {
  getBeanImage,
  getCurrencyImage,
  getCurrencySymbol,
} from "../util/util";
import TopBar from "../../../components/TopBar";
// import Alarm from "../component/Alarm";
import TargetAmountProgressBar from "../component/TargetAmountProgressBar";
import ChatBot from "../component/ChatBot";
import Loading from "../component/Loading";
import ChangeAccountNameModal from "../modal/ChangeAccountName";
import ChangeTargetAmountModal from "../modal/ChangeTargetAmountModal";

const DomesticTravelAccountPage: React.FC = () => {
  const { accountId } = useParams(); // Path Variable

  const nav = useNavigate();

  const [loading1, setLoading1] = useState(true); // 서버에서 데이터 수신 여부 체크
  const [loading2, setLoading2] = useState(true); // 서버에서 데이터 수신 여부 체크

  const [travelAccountData, setTravelAccountData] =
    useState<TravelAccountData>(); // 한화 여행통장 + 외화 여행통장 상태관리

  const [travelAccountMemberAmountData, setTravelAccountMemberAmountData] =
    useState<TravelAccountMemberAmountData>(); // 여행통장 멤버 상태관리

  const [accountName, setAccountName] = useState<string>(); // 여행통장 이름 상태관리
  const [targetAmount, setargetAmount] = useState<number>(0); // 목표 금액 상태 상태관리
  const [collectedAmount, setCollectedAmount] = useState<number>(0); // 현재 모인 금액 상태 상태관리

  // 여행통장 이름 변경 모달
  const [isChangeAccountNameModalOpen, setIsChangeAccountNameModalOpen] =
    useState(false);

  const openChangeAccountNameModal = () =>
    setIsChangeAccountNameModalOpen(true);
  const closeChangeAccountNameModal = () =>
    setIsChangeAccountNameModalOpen(false);

  // 목표 관리 모달
  const [isChangeTargetAmountModalOpen, setIsChangeTargetAmountModalOpen] =
    useState(false);

  const openChangeTargetAmountModal = () =>
    setIsChangeTargetAmountModalOpen(true);
  const closeChangeTargetAmountModal = () =>
    setIsChangeTargetAmountModalOpen(false);

  // 함수 모음
  const handleNBbang = () => {
    // alert("친구들과 N빵하기 누름!!!!!!");
    nav("/travel/split"); // 비밀번호 입력 페이지로 이동
  };

  const handlePayment = () => {
    alert("다함께 결제해요 누름!!!!!!");
  };

  const handleExpenseTracker = () => {
    alert("예산관리 가계부 누름!!!!!!");
  };

  // Travel 서버 여행통장 조회 API fetch 요청
  useEffect(() => {
    const fetchTravelAccountData = async () => {
      try {
        const response = await client().get(`/api/travel/${accountId}`);
        setTravelAccountData(response.data);
        setAccountName(response.data.accountName);
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

  // Travel 서버 목표금액 전체 조회 (role 추가하기) API fetch 요청
  useEffect(() => {
    const fetchTravelAccountMemberAmountData = async () => {
      try {
        const response = await client().get(
          `/api/travel/targetAmount/${accountId}`,
        );
        setTravelAccountMemberAmountData(response.data);
        setargetAmount(response.data.targetAmount);
        setCollectedAmount(response.data.amount);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading2(false);
      }
    };

    if (accountId) {
      fetchTravelAccountMemberAmountData();
    }
  }, [accountId]);

  // 로딩 중이면 로딩 스피너 표시
  if (loading1 || loading2) {
    return <Loading />;
  }

  return (
    <div className="h-full relative bg-zinc-100">
      {/* 네비게이션 바 */}
      <div className="pt-16">
        <TopBar isWhite={false} isLogo />
      </div>

      {/* 알림 */}
      {/* <div className="px-4 py-2">
        <Alarm />
      </div> */}

      {/* 여행통장 목록 */}
      <div className="px-4 py-2">
        <div className="rounded-2xl p-4 bg-white">
          {/* 여행통장 목록 상단 */}
          <div className="flex justify-between p-2">
            <div className="text-lg font-bold">{accountName}</div>
            <div>
              <button type="button" onClick={openChangeAccountNameModal}>
                <img src={settings} alt={settings} className="w-6 h-6" />
              </button>
            </div>
          </div>

          {/* <button
          type="button"
          onClick={() =>
            nav(`/accounts/travel/domestic/${parentAccountId}/detail`)
          }
        > */}

          {/* 여행통장 목록 중단 */}
          <div className="py-4">
            {travelAccountData?.account.map((account) => (
              <div key={account.accountId}>
                <button
                  type="button"
                  onClick={() => {
                    if (account.exchangeCurrency === "KRW") {
                      nav(
                        `/accounts/travel/domestic/${account.accountId}/detail`,
                      );
                    } else {
                      nav(
                        `/accounts/travel/foreign/${account.accountId}/detail`,
                      );
                    }
                  }}
                  className="flex justify-between items-center w-full py-4"
                >
                  <div className="flex items-center">
                    <img
                      src={getCurrencyImage(account.exchangeCurrency)}
                      alt={account.exchangeCurrency}
                      className="w-6 h-6"
                    />
                    <div className="ml-4 font-bold">{account.country}</div>
                  </div>
                  <div className="font-bold">
                    {getCurrencySymbol(account.exchangeCurrency)}
                    {account.accountBalance.toLocaleString()}
                  </div>
                </button>
              </div>
            ))}
          </div>

          {/* 여행통장 목록 중단
          <div className="py-4">
            {travelAccountData?.account.map((account) => (
              <div
                key={account.accountId}
                className="flex justify-between items-center px-2 py-4"
              >
                <img
                  src={getCurrencyImage(account.exchangeCurrency)}
                  alt={account.exchangeCurrency}
                  className="w-6 h-6"
                />
                <div className="flex-grow font-bold ml-4">
                  {account.country}
                </div>
                <div className="font-bold">
                  {getCurrencySymbol(account.exchangeCurrency)}
                  {account.accountBalance.toLocaleString()}
                </div>
              </div>
            ))}
          </div> */}

          {/* 여행통장 목록 하단 */}
          <div>
            <button
              type="button"
              onClick={() => {
                nav(`/accounts/travel/foreign/${accountId}/create`);
              }}
              className="btn-lg w-[90%] mx-auto block"
            >
              외화 추가하기
            </button>
          </div>
        </div>
      </div>

      {/* 여행통장 멤버 목록 */}
      <div className="px-4 py-2">
        <div className="rounded-2xl p-4 bg-white">
          <div className="flex justify-end">
            <div className="text-right text-xs mr-2">
              <button type="button" onClick={openChangeTargetAmountModal}>
                목표관리
              </button>
            </div>
            <div className="text-right text-xs">
              <button
                type="button"
                onClick={() =>
                  nav(`/accounts/travel/domestic/${accountId}/members`)
                }
              >
                멤버관리
              </button>
            </div>
          </div>
          <div className="py-4">
            <TargetAmountProgressBar
              targetAmount={targetAmount}
              collectedAmount={collectedAmount}
            />
            <div className="flex justify-between mt-2">
              <div className="text-xs">₩{collectedAmount.toLocaleString()}</div>
              <div className="text-xs">₩{targetAmount.toLocaleString()}</div>
            </div>
          </div>
          <div className="flex flex-wrap justify-center">
            {travelAccountMemberAmountData?.memberList.map((member) => (
              <div
                key={member.userId}
                className="flex flex-col items-center p-2"
              >
                <img
                  src={getBeanImage(member.role)}
                  alt={member.role}
                  className="w-10 h-10"
                />
                <div className="text-xs">{member.userName}</div>
                <div className="text-xs">
                  ₩{(member.amount ?? 0).toLocaleString()}
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* 여행통장 기능 목록 */}
      <div className="px-4 py-2">
        <div className="flex justify-between rounded-2xl">
          {/* 친구들과 N빵하기 */}
          <div>
            <button
              type="button"
              onClick={handleNBbang}
              className="flex flex-col items-center bg-white rounded-3xl px-6 py-2"
            >
              <div>
                <img src={dollarCoin} alt="dollarCoin" className="w-10 h-10" />
              </div>
              <div className="text-sm">친구들과</div>
              <div className="text-sm font-bold">N빵하기</div>
            </button>
          </div>
          {/* 다함께 결제해요 */}
          <div>
            <button
              type="button"
              onClick={handlePayment}
              className="flex flex-col items-center bg-white rounded-3xl px-6 py-2"
            >
              <div>
                <img src={moneyBox} alt="moneyBox" className="w-10 h-10" />
              </div>
              <div className="text-sm">다함께</div>
              <div className="text-sm font-bold">결제해요</div>
            </button>
          </div>
          {/* 예산관리 가계부 */}
          <div>
            <button
              type="button"
              onClick={handleExpenseTracker}
              className="flex flex-col items-center bg-white rounded-3xl px-6 py-2"
            >
              <div>
                <img src={eventNote} alt="eventNote" className="w-10 h-10" />
              </div>
              <div className="text-sm">예산관리</div>
              <div className="text-sm font-bold">가계부</div>
            </button>
          </div>
        </div>
      </div>

      {/* 챗봇 */}
      <div className="px-4 py-2">
        <ChatBot />
      </div>

      {/* 여행통장 이름 변경 모달 */}
      {isChangeAccountNameModalOpen ? (
        <div className="absolute inset-0 flex items-center justify-center bg-gray-900 bg-opacity-50">
          <ChangeAccountNameModal
            accountId={accountId}
            accountName={accountName}
            onAccountNameChange={setAccountName}
            onClose={closeChangeAccountNameModal}
          />
        </div>
      ) : null}

      {/* 목표 관리 모달 */}
      {isChangeTargetAmountModalOpen ? (
        <div className="absolute inset-0 flex items-end py-8 bg-gray-900 bg-opacity-50">
          <div className="w-full">
            <ChangeTargetAmountModal
              accountId={accountId}
              targetAmount={targetAmount.toString()}
              onTargetAmountChange={setargetAmount}
              onClose={closeChangeTargetAmountModal}
            />
          </div>
        </div>
      ) : null}
    </div>
  );
};

export default DomesticTravelAccountPage;
