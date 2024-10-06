import { useState } from "react";
import { useNavigate } from "react-router-dom";

import axios from "axios";
import prevIcon from "../../assets/icon/prevIcon.png";
import client from "../../client";

function JoinPage() {
  const navigate = useNavigate();

  const [name, setName] = useState<string | null>(null);

  const [email, setEmail] = useState<string | null>(null);
  const [inputEmail, setInputEmail] = useState<string>();
  const [selectPath, setSelectPath] = useState<string>();
  const [checkEmail, setCheckEmail] = useState<number | null>(null);

  const [password, setPassword] = useState<string | null>(null);
  const [inputPassword, setInputPassword] = useState<string>();
  const [checkPassword, setCheckPassword] = useState<boolean>();
  const [confirmPWStatus, setConfirmPWStatus] = useState<boolean>(true);

  const onChangeName = (e: React.ChangeEvent<HTMLInputElement>) => {
    setName(e.target.value);
  };

  const onChangeEmail = (e: React.ChangeEvent<HTMLInputElement>) => {
    setInputEmail(e.target.value);
  };

  const onSelectEmailPath = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectPath(e.target.value);
  };

  const onCheckEmail = async () => {
    if (!inputEmail || !selectPath) return;

    const fullEmail = `${inputEmail}@${selectPath}`;

    const response = await client().get(`/api/user/email/${fullEmail}`);

    if (response.data) {
      alert("이미 사용 중인 이메일입니다.");
      setEmail(null);
    } else {
      setCheckEmail(1);
      setEmail(fullEmail);
    }
  };

  const onChangePassword = (e: React.ChangeEvent<HTMLInputElement>) => {
    const pw = e.target.value;

    // 비밀번호 규칙: 8자 이상, !@*~$% 중 하나 이상의 특수문자 포함
    const passwordRegex = /^(?=.*[!@*~$%]).{8,}$/;
    if (!passwordRegex.test(pw)) {
      setCheckPassword(false);
      setPassword(null);
    } else {
      setCheckPassword(true);
      setInputPassword(pw);
    }
  };

  const confirmPassword = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.value === inputPassword) {
      setConfirmPWStatus(true);
      setPassword(inputPassword);
    } else {
      setConfirmPWStatus(false);
      setPassword(null);
    }
  };

  const onClickJoinBtn = async () => {
    if (!email && inputEmail) {
      alert("이메일 중복확인을 진행해주세요.");
      return;
    }

    if (!email || !password || !name) {
      alert("입력하지 않은 필드가 있습니다.");
      return;
    }

    const response = await axios.post(
      `${process.env.REACT_APP_END_POINT}/api/user/signup`,
      { email, password, name },
    );

    if (response.status === 200) {
      navigate("/login");
    }
  };

  return (
    <div className="h-[100vh] relative mt-5">
      <div className="flex">
        <img src={prevIcon} alt="이전버튼" className="w-[17px] h-[28px]" />
        <h2 className="text-lg m-auto">회원가입</h2>
      </div>
      <div className="mx-2 mt-16 ">
        <div>
          <input
            type="text"
            name="name"
            id="name"
            placeholder="이름"
            className="border-b-2 border-primary text-base pl-2 mb-5 w-full"
            onChange={onChangeName}
          />
          <div className="flex items-center justify-between">
            <input
              type="text"
              name="email"
              id="email"
              placeholder="아이디"
              className="w-[130px] border-b-2 border-primary text-base pl-2"
              onChange={onChangeEmail}
            />
            <div>@</div>
            <select
              className="w-[100px] border-b-2 border-primary pb-1"
              onChange={onSelectEmailPath}
            >
              <option value="" disabled selected>
                선택
              </option>
              <option value="gmail.com">gmail.com</option>
              <option value="naver.com">naver.com</option>
              <option value="daum.net">daum.net</option>
            </select>
            <button
              type="button"
              className="btn-light-md"
              onClick={onCheckEmail}
            >
              중복확인
            </button>
          </div>
          {checkEmail && (
            <span className="mb-5 text-sm text-primary">
              사용 가능한 이메일입니다.
            </span>
          )}
        </div>
        <div className="my-5">
          <input
            type="password"
            name="password"
            id="password"
            className="border-b-2 border-primary text-base pl-2 w-full focus:outline-none"
            placeholder="비밀번호"
            onChange={onChangePassword}
          />
          {!checkPassword && (
            <span className="text-sm text-gray-300 font-semibold">
              * 비밀번호 (특수문자 포함 8글자 이상)
            </span>
          )}
        </div>
        <div>
          <input
            type="password"
            name="password"
            id="password"
            className="border-b-2 border-primary text-base pl-2 w-full focus:outline-none"
            placeholder="비밀번호 확인"
            onChange={confirmPassword}
          />
          {!confirmPWStatus && (
            <span className="text-sm text-gray-300 font-semibold">
              비밀번호가 일치하지 않습니다.
            </span>
          )}
        </div>
      </div>
      <button
        type="button"
        onClick={onClickJoinBtn}
        className={`w-full absolute bottom-10 ${
          email && password ? "btn-lg" : "btn-gray-lg cursor-not-allowed"
        }`}
      >
        다음 단계
      </button>
    </div>
  );
}

export default JoinPage;
