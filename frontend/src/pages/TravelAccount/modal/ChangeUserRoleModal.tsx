import React from "react";
import client from "../../../client";
import { TravelAccountMember } from "../type/type";
import { getBeanImage } from "../util/util";

interface ChangeUserRoleModalProps {
  accountId: string | undefined;
  memberInfo: TravelAccountMember;
  onMemberInfoChange: (updatedMember: TravelAccountMember) => void;
  onClose: () => void;
}

const ChangeUserRoleModal: React.FC<ChangeUserRoleModalProps> = ({
  accountId,
  memberInfo,
  onMemberInfoChange,
  onClose,
}) => {
  // 확인 버튼을 눌렀을 때 서버로 POST 요청 전송
  const handleUpdateUserRole = () => {
    const newRole = memberInfo.role === "PAYER" ? "NONE_PAYER" : "PAYER";

    const fetchUpdateUserRole = async () => {
      try {
        await client().post("/api/travel/role", {
          userId: memberInfo.userId,
          accountId,
          role: newRole,
        });
        alert("멤버 권한이 성공적으로 변경 되었습니다!");
        onMemberInfoChange({ ...memberInfo, role: newRole });
        onClose();
      } catch (error) {
        alert("멤버 권한 변경에 실패했습니다.");
        console.error(error);
      }
    };

    fetchUpdateUserRole();
  };

  // 역할에 따라 메시지 결정
  const getRoleMessage = (role: string) => {
    switch (role) {
      case "PAYER":
        return "결제불가";
      case "NONE_PAYER":
        return "결제가능";
      default:
        return "";
    }
  };

  // 역할에 따라 글씨 색상 결정
  const getRoleTextColor = (role: string) =>
    role === "PAYER" ? "text-red-500" : "text-green-500";

  return (
    <div className="bg-white p-4 rounded-t-3xl">
      <div className="flex flex-col items-center">
        <div className="p-4">
          <img
            src={getBeanImage(memberInfo.role)}
            alt={memberInfo.role}
            className="w-12 h-12"
          />
          <div className="text-xl text-center">{memberInfo.userName}</div>
        </div>
        <div className="flex">
          <div className="font-bold">{memberInfo.userName}</div>
          <div>님의 결제 권한을</div>
        </div>
        <div className="flex">
          <div className={`font-bold ${getRoleTextColor(memberInfo.role)}`}>
            {getRoleMessage(memberInfo.role)}
          </div>
          <div>(으)로 설정하시겠습니까?</div>
        </div>
      </div>
      <div className="flex justify-between p-4">
        <button
          type="button"
          onClick={onClose}
          className="btn-light-lg w-1/2 m-4 text-center"
        >
          돌아 가기
        </button>
        <button
          type="button"
          onClick={handleUpdateUserRole}
          className="btn-lg w-1/2 m-4 text-center"
        >
          설정 완료
        </button>
      </div>
    </div>
  );
};

export default ChangeUserRoleModal;
