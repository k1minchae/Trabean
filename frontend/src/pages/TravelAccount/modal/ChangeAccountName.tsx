import React, { useState } from "react";
import client from "../../../client";

interface ChangeAccountNameModalProps {
  accountId: string | undefined;
  accountName: string | undefined;
  onAccountNameChange: (updatedAccountName: string) => void;
  onClose: () => void;
}

const ChangeAccountNameModal: React.FC<ChangeAccountNameModalProps> = ({
  accountId,
  accountName,
  onAccountNameChange,
  onClose,
}) => {
  const [newAccountName, setNewAccountName] = useState(accountName); // 변경할 이름 상태관리

  // 입력 값이 변경될 때 상태 업데이트
  const handleChangeAccountName = (e: React.ChangeEvent<HTMLInputElement>) => {
    setNewAccountName(e.target.value);
  };

  // 확인 버튼을 눌렀을 때 서버로 PUT 요청 전송
  const handleUpdateAccountName = async () => {
    if (!newAccountName) {
      alert("계좌 이름을 입력하세요.");
      return;
    }

    const fetchUpdateAccountName = async () => {
      try {
        await client().put("/api/travel/change/accountName", {
          accountId,
          accountName: newAccountName,
        });
        // alert("계좌 이름이 성공적으로 변경되었습니다!");
        onAccountNameChange(newAccountName);
        onClose();
      } catch (error) {
        alert("계좌 이름 변경에 실패했습니다.");
        console.error(error);
      }
    };

    fetchUpdateAccountName();
  };

  return (
    <div className="bg-white p-6 rounded-3xl">
      <h2 className="text-xl mb-4">여행통장 이름 변경</h2>
      <input
        type="text"
        value={newAccountName}
        onChange={handleChangeAccountName}
        className="border p-2 w-full mb-4"
      />
      <div className="flex justify-between">
        <button
          type="button"
          onClick={onClose}
          className="btn-light-lg w-1/2 mr-2 text-center"
        >
          취소
        </button>
        <button
          type="button"
          onClick={handleUpdateAccountName}
          className="btn-lg w-1/2 ml-2 text-center"
        >
          확인
        </button>
      </div>
    </div>
  );
};

export default ChangeAccountNameModal;
