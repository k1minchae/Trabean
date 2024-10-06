import React, { useState } from "react";
import client from "../../../client";

interface InviteMemberModalProps {
  accountId: string | undefined;
  onClose: () => void;
}

const InviteMemberModal: React.FC<InviteMemberModalProps> = ({
  accountId,
  onClose,
}) => {
  const [email, setEmail] = useState(""); // 변경할 이메일 상태관리

  // 입력 값이 변경될 때 상태 업데이트
  const handleChangeEmail = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value);
  };

  // 확인 버튼을 눌렀을 때 서버로 POST 요청 전송
  const handleInviteMember = async () => {
    if (!email) {
      alert("이메일을 입력해주세요.");
      return;
    }

    const fetchInviteMember = async () => {
      try {
        await client().post("/api/travel/invitation", {
          accountId,
          email,
        });
        alert("초대가 완료되었습니다.");
        onClose();
      } catch (error) {
        alert("초대 요청에 실패했습니다.");
        console.error(error);
      }
    };

    fetchInviteMember();
  };

  return (
    <div className="bg-white p-6 rounded-3xl">
      <h2 className="text-xl mb-4">멤버 초대</h2>
      <input
        type="email"
        value={email}
        onChange={handleChangeEmail}
        placeholder="이메일 입력"
        className="border p-2 w-full mb-4"
      />
      <div className="flex justify-between">
        <button
          type="button"
          onClick={onClose}
          className="btn-light-lg w-1/2 mr-2"
        >
          취소
        </button>
        <button
          type="button"
          onClick={handleInviteMember}
          className="btn-lg w-1/2 ml-2"
        >
          확인
        </button>
      </div>
    </div>
  );
};

export default InviteMemberModal;
