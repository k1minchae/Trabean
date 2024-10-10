import { useEffect, useState } from "react";
import client from "../../client";
import Item from "./NotificationPage.Item";

import xIcon from "../../assets/icon/xIcon.png";

type NotificationType = {
  notificationId: number;
  senderId: number;
  accountId: number;
  isRead: boolean;
  notificationType: string;
  amount: number;
  createTime: string;
};

interface NotificationModalProps {
  hidden: boolean;
  changeNotiHidden: (hidden: boolean) => void;
}

function Notification({ hidden, changeNotiHidden }: NotificationModalProps) {
  const [notiList, setNotiList] = useState<Array<NotificationType>>();

  useEffect(() => {
    const getNotification = async () => {
      const response = await client().get(`/api/notifications`);
      setNotiList(response.data);
    };

    getNotification();
  }, []);

  return (
    <div
      className={`font-bold absolute w-[360px] h-[350px] bottom-[60px] bg-gray-100 bg-opacity-90 px-2 py-5 rounded-t-2xl ${hidden ? "hidden" : ""}`}
    >
      <div className="flex justify-between items-center mx-5 mb-3">
        <h1 className="text-lg">알림</h1>
        <img
          src={xIcon}
          alt="창닫기"
          className="w-[10px] cursor-pointer"
          onClick={() => changeNotiHidden(hidden)}
          role="presentation"
        />
      </div>
      <div className="h-[90%] overflow-auto scrollbar-hide">
        {notiList ? (
          <div className="px-2">
            {notiList.map((noti) => (
              <Item item={noti} key={noti.createTime} />
            ))}
          </div>
        ) : (
          <span>알림 내역이 없습니다.</span>
        )}
      </div>
    </div>
  );
}

export default Notification;
