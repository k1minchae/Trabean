import soyIcon from "../../assets/icon/soyIcon.png";
import arrowRIcon from "../../assets/icon/arrowRIcon.png";

type Account = {
  accountId: number;
  accountNo: string;
  accountName: string;
  bankName: string;
  accountBalance: number;
};

interface AccountProps {
  account: Account;
}

function Account({ account }: AccountProps) {
  return (
    <div className="flex items-center px-3 my-3">
      <img src={soyIcon} alt="통장아이콘" className="w-[30px]" />
      <div className="flex flex-col w-[170px] truncate text-ellipsis ml-2">
        <h5 className="text-sm font-bold">{account.accountName}</h5>
        <span className="text-gray-700 text-[8px]">{account.accountNo}</span>
      </div>
      <div className="font-bold text-sm ml-3 w-[80px]">
        {account.accountBalance}원
      </div>
      <img
        src={arrowRIcon}
        alt="통장상세보기아이콘"
        className="w-[18px] right-0"
      />
    </div>
  );
}

export default Account;
