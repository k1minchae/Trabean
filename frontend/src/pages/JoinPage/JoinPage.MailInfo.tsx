import TopBar from "../../components/TopBar";

import bean from "../../assets/greenBean.png";

function MailInfo() {
  return (
    <>
      <TopBar isLogo isWhite />
      <div className="flex flex-col items-center pt-28 relative h-full">
        <img src={bean} alt="캐릭터이미지" className="w-[187px] my-12" />
        <p className="text-base w-[190px] text-center">
          메일 내 인증버튼을 클릭하여 회원가입을 완료해주세요.
        </p>
        <button type="button" className="btn-lg w-[80%] absolute bottom-5">
          확인
        </button>
      </div>
    </>
  );
}

export default MailInfo;
