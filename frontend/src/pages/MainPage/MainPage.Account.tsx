import { useNavigate } from "react-router-dom";

import { useEffect, useState } from "react";
import soyIcon from "../../assets/icon/soyIcon.png";
import arrowRIcon from "../../assets/icon/arrowRIcon.png";
import { formatNumberWithCommas } from "../../utils/formatNumber";

type Account = {
  accountId: number;
  accountNo: string;
  accountName: string;
  bankName: string;
  accountBalance: number;
  accountType: string;
};

interface AccountProps {
  account: Account;
}

function Account({ account }: AccountProps) {
  const [url, setUrl] = useState<string>();

  const navigate = useNavigate();

  useEffect(() => {
    const settingUrl = () => {
      if (account.accountType === "PERSONAL") {
        setUrl(`/accounts/personal/${account.accountId}/detail`);
      } else if (account.accountType === "DOMESTIC") {
        setUrl(`/accounts/travel/domestic/${account.accountId}`);
      }
    };

    settingUrl();
  }, [account]);

  return (
    <div
      className="flex items-center px-3 my-3"
      onClick={() => url && navigate(url)}
      role="presentation"
    >
      <img src={soyIcon} alt="통장아이콘" className="w-[30px] mr-2" />
      <div className="w-full flex justify-between items-center">
        <div className="flex flex-col">
          <h5 className="text-sm font-bold truncate text-ellipsis">
            {account.accountName}
          </h5>
          <span className="text-gray-700 text-[8px]">{account.accountNo}</span>
        </div>
        <div className="flex font-bold text-sm ">
          {formatNumberWithCommas(account.accountBalance)}원
          <img
            src={arrowRIcon}
            alt="통장상세보기아이콘"
            className="w-[18px] h-[18px] right-0"
          />
        </div>
      </div>
    </div>
  );
}

export default Account;
