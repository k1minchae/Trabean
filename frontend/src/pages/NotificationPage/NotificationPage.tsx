import { useEffect, useState } from "react";
import TopBar from "../../components/TopBar";
import client from "../../client";
import Item from "./NotificationPage.Item";

type NotificationType = {
  notificationId: number;
  senderId: number;
  accountId: number;
  isRead: boolean;
  notificationType: string;
  amount: number;
  createTime: string;
};

function Notification() {
  const [notiList, setNotiList] = useState<Array<NotificationType>>();

  useEffect(() => {
    const getNotification = async () => {
      const response = await client().get(`/api/notifications`);
      setNotiList(response.data);
    };

    getNotification();
  }, []);

  return (
    <>
      <TopBar isLogo isWhite />
      <div className="mt-16 font-bold">
        <h1 className="text-lg ml-5 mb-2">알림</h1>
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
    </>
  );
}

export default Notification;
