import bean from "../../assets/bean.png";
import userIcon from "../../assets/icon/userIcon.png";
import keyIcon from "../../assets/icon/keyIcon.png";

function LoginPage() {
  return (
    <div className="bg-primary-light w-full h-full flex flex-col justify-center items-center">
      <img src={bean} alt="아이콘" className="w-[40px] mb-7" />
      <div className="bg-white w-[290px] h-[303px] rounded-3xl flex flex-col justify-between items-center py-7">
        <h3 className="text-gray-700 font-semibold text-4">Login</h3>
        <div className="flex items-center border-[1.5px] border-primary-light border-solid rounded-md px-2">
          <img src={userIcon} alt="사용자 아이콘" className="w-6 mr-2" />
          <input
            type="text"
            name="email"
            id="email"
            placeholder="아이디(이메일)"
            className="h-9 text-xs focus:outline-none"
          />
        </div>
        <div className="flex items-center border-[1.5px] border-primary-light border-solid rounded-md px-2">
          <img src={keyIcon} alt="비밀번호 아이콘" className="w-6 mr-2" />
          <input
            type="text"
            name="password"
            id="password"
            placeholder="비밀번호"
            className="h-9 text-xs focus:outline-none"
          />
        </div>
        <button type="button" className="btn-md w-[75%]">
          로그인
        </button>
        <button type="button" className="btn-gray-md w-[75%]">
          회원가입
        </button>
      </div>
    </div>
  );
}

export default LoginPage;
