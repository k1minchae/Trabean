import soyIcon from "../../assets/icon/soyIcon.png";
import arrowRIcon from "../../assets/icon/arrowRIcon.png";

function Account() {
  return (
    <div className="flex justify-between items-center px-4">
      <img src={soyIcon} alt="통장아이콘" className="w-[30px]" />
      <div className="flex flex-col">
        <h5 className="text-sm font-bold">개인 입출금통장</h5>
        <span className="text-gray-700 text-[8px]">1002-555-139750</span>
      </div>
      <div className="font-bold text-sm">10,000원</div>
      <img src={arrowRIcon} alt="통장상세보기아이콘" className="w-[18px]" />
    </div>
  );
}

export default Account;
