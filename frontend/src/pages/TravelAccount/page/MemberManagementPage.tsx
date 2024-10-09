import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import account from "../../../assets/account.png";
import client from "../../../client";
import {
  TravelAccountData,
  TravelAccountMember,
  TravelAccountMemberData,
} from "../type/type";
import { formatUserRole, getBeanImage } from "../util/util";
import TopBar from "../../../components/TopBar";
import Loading from "../component/Loading";
import ChangeUserRoleModal from "../modal/ChangeUserRoleModal";
import InviteMemberModal from "../modal/InviteMemberModal";

const MemberManagementPage: React.FC = () => {
  const { accountId } = useParams(); // Path Variable

  const nav = useNavigate();

  const [loading1, setLoading1] = useState(true); // 서버에서 데이터 수신 여부 체크
  const [loading2, setLoading2] = useState(true); // 서버에서 데이터 수신 여부 체크

  const [travelAccountData, setTravelAccountData] =
    useState<TravelAccountData>(); // 한화 여행통장 + 외화 여행통장 상태관리

  const [travelAccountMemberData, setTravelAccountMemberData] =
    useState<TravelAccountMemberData>(); // 여행통장 멤버 상태관리

  const [memberInfo, setMemberInfo] = useState<TravelAccountMember>(); // 선택한 멤버 상태관리

  // 부모 컴포넌트에서 전체 member 리스트를 업데이트하도록 수정 (화면에 반영이 안되는 state는 리렌더링이 안되는 이슈)
  const handleUpdateMemberInfo = (updatedMember: TravelAccountMember) => {
    setTravelAccountMemberData((prevData) => {
      if (prevData) {
        const updatedMembers = prevData.members.map((member) =>
          member.userId === updatedMember.userId ? updatedMember : member,
        );
        return {
          ...prevData,
          members: updatedMembers,
        };
      }
      return prevData;
    });
  };

  // 유저 권한 변경 모달
  const [isChangeUserRoleModalOpen, setIsChangeUserRoleModalOpen] =
    useState(false);

  const openChangeUserRoleModal = (member: TravelAccountMember) => {
    setIsChangeUserRoleModalOpen(true);
    setMemberInfo(member);
  };

  const closeChangeUserRoleModal = () => {
    setIsChangeUserRoleModalOpen(false);
  };

  // 멤버 초대 모달
  const [isChangeInviteModalOpen, setIsChangeInviteModalOpen] = useState(false);

  const openChangeInviteModal = () => {
    setIsChangeInviteModalOpen(true);
  };

  const closeChangeInviteModal = () => {
    setIsChangeInviteModalOpen(false);
  };

  // Travel 서버 여행통장 조회 API fetch 요청
  useEffect(() => {
    const fetchTravelAccountData = async () => {
      try {
        const response = await client().get(`/api/travel/${accountId}`);
        setTravelAccountData(response.data);
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
  useEffect(() => {
    const fetchTravelAccountData = async () => {
      try {
        const response = await client().get(
          `/api/accounts/travel/domestic/${accountId}/members`,
        );
        setTravelAccountMemberData(response.data);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading2(false);
      }
    };

    if (accountId) {
      fetchTravelAccountData();
    }
  }, [accountId]);

  // 로딩 중이면 로딩 스피너 표시
  if (loading1 || loading2) {
    return <Loading />;
  }

  return (
    <div className="h-full relative">
      {/* 네비게이션 바 */}
      <div className="pt-16">
        <TopBar page="멤버 관리" isWhite isLogo={false} />
      </div>

      {/* 통장 정보 */}
      <div className="px-4 py-2 mb-8 border-b border-gray-300">
        <button
          type="button"
          onClick={() => nav(`/accounts/travel/domestic/${accountId}/detail`)}
        >
          <div className="flex items-center mb-4">
            <img src={account} alt="account" className="w-12 h-12 mr-2" />
            <div>
              <div className="text-xl text-left">
                {travelAccountData?.accountName}
              </div>
              <div className="text-sm text-left">
                {travelAccountData?.bankName} {travelAccountData?.accountNo}
              </div>
            </div>
          </div>
        </button>
      </div>

      {/* 멤버 정보 */}
      <div className="px-4 py-2">
        <div className="text-xs">{travelAccountMemberData?.memberCount}명</div>
        <div>모임원</div>
      </div>

      {/* 멤버 목록 */}
      <div className="px-4 py-2">
        <div>
          {travelAccountMemberData?.members.map((member) => {
            const roleText = formatUserRole(member.role);
            const isNonPayer = roleText === "결제불가";
            const isLeader = roleText === "모임장";
            const isCurrentUser =
              member.userId === travelAccountMemberData?.userId;

            return (
              <button
                type="button"
                key={member.userId}
                className="flex py-4"
                onClick={() => !isLeader && openChangeUserRoleModal(member)}
                disabled={isLeader}
              >
                <img
                  src={getBeanImage(member.role)}
                  alt={member.role}
                  className="w-12 h-12 mr-4"
                />
                <div className="text-left">
                  <div className="mb-1">{member.userName}</div>
                  <div
                    className={`font-bold text-sm ${isNonPayer ? "text-red-500" : "text-green-500"}`}
                  >
                    {isCurrentUser ? "나 / " : ""}
                    {roleText}
                  </div>
                </div>
              </button>
            );
          })}
        </div>
      </div>

      {/* 초대하기 */}
      <div>
        <button
          type="button"
          onClick={openChangeInviteModal}
          className="btn-lg absolute bottom-24 left-0 right-0 w-[90%] mx-auto block"
        >
          초대하기
        </button>
      </div>

      {/* 유저 권한 변경 모달 */}
      {isChangeUserRoleModalOpen && memberInfo ? (
        <div className="absolute inset-0 flex items-end py-8 bg-gray-900 bg-opacity-50">
          <div className="w-full">
            <ChangeUserRoleModal
              accountId={accountId}
              memberInfo={memberInfo}
              onMemberInfoChange={handleUpdateMemberInfo}
              onClose={closeChangeUserRoleModal}
            />
          </div>
        </div>
      ) : null}

      {/* 멤버 초대 모달 */}
      {isChangeInviteModalOpen ? (
        <div className="absolute inset-0 flex items-center justify-center bg-gray-900 bg-opacity-50">
          <InviteMemberModal
            accountId={accountId}
            onClose={closeChangeInviteModal}
          />
        </div>
      ) : null}
    </div>
  );
};

export default MemberManagementPage;
