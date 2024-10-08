import { useEffect, useState } from "react";
import bean from "../../assets/bean.png";
import client from "../../client";

type Notification = {
  notificationId: number;
  senderId: number;
  accountId: number;
  isRead: boolean;
  notificationType: string;
  amount: number;
  createTime: string;
};

interface NotificationProps {
  item: Notification;
}

function Item({ item }: NotificationProps) {
  const [accountName, setAccountName] = useState<string>();
  const [sender, setSender] = useState<string>();
  const [infoText, setInfoText] = useState<string>();

  const date = item.createTime.split("T")[0].split("-");

  const onClickNoti = () => {
    client().patch(`/api/notifications/${item.notificationId}`);
  };

  useEffect(() => {
    const getAccountName = async () => {
      const response = await client().get(
        `/api/travel/account-name/${item.accountId}`,
      );
      setAccountName(response.data);
    };

    const getSenderName = async () => {
      const response = await client().get(`/api/user/name/${item.senderId}`);
      setSender(response.data.userName);
    };

    getAccountName();
    getSenderName();
  }, [item.accountId, item.senderId]);

  useEffect(() => {
    if (item.notificationType === "INVITE") {
      setInfoText(`${sender}님이 ${accountName} 여행통장에 초대했어요!`);
    } else if (item.notificationType === "DEPOSIT") {
      setInfoText(
        `${sender}님이 ${accountName} 통장에 ${item.amount}원을 입금했어요!`,
      );
    } else if (item.notificationType === "WITHDRAW") {
      setInfoText(
        `${sender}님이 ${accountName} 통장에서 ${item.amount}원을 출금했어요!`,
      );
    } else if (item.notificationType === "PAYMENT") {
      setInfoText(
        `${sender}님이 ${accountName} 통장에서 ${item.amount}원을 지불했어요!`,
      );
    } else if (item.notificationType === "AUTH") {
      setInfoText(`${accountName} 통장 인증번호는 ${item.amount}입니다.`);
    }
  }, [accountName, sender, item.notificationType, item.amount]);

  return (
    <div
      className={`flex px-2 py-3 ${item.isRead ? "" : "bg-primary-light"} rounded-lg mb-2`}
      onClick={onClickNoti}
      role="presentation"
    >
      <img src={bean} alt="아이콘" className="w-[25px] h-[25px] mr-2" />
      <div className="w-full">
        <div className="flex items-cente justify-between w-full">
          <div>
            <span className="text-base mr-1">{accountName}</span>
            <span className="text-base text-gray-700">통장</span>
          </div>
          <div className="text-xs ml-3">{`${date[1]}월 ${date[2]}일`}</div>
        </div>
        <div className="text-xs">{infoText}</div>
      </div>
    </div>
  );
}

export default Item;
