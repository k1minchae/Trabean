import axios from "axios";
import { useState } from "react";

import { useNavigate } from "react-router-dom";
import bean from "../../assets/bean.png";
import userIcon from "../../assets/icon/userIcon.png";
import keyIcon from "../../assets/icon/keyIcon.png";
import useAuthStore from "../../store/useAuthStore";
import client from "../../client";

function LoginPage() {
  const navigate = useNavigate();

  const [email, setEmail] = useState<string | null>(null);
  const [password, setPassword] = useState<string | null>(null);

  const setAccessToken = useAuthStore((state) => state.setAccessToken);

  const inputEmail = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value);
  };

  const inputPassword = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(e.target.value);
  };

  const getMainPaymentAccount = async () => {
    try {
      // API 요청을 통해 결제 계좌 ID 가져오기
      const response = await client().get("/api/payments/main-account");
      const { paymentAccountId } = response.data;
      console.log(response.data);
      localStorage.setItem("paymentAccountId", paymentAccountId);
      // Store에 paymentAccountId 저장
      useAuthStore.setState({ paymentAccountId });
    } catch (error) {
      console.error("main payment account 불러올 때 에러 발생:", error);
      localStorage.setItem("paymentAccountId", "117");
    }
  };

  const onClickLogin = async () => {
    const response = await axios.post(
      `${process.env.REACT_APP_END_POINT}/api/user/login`,
      { email, password },
      { withCredentials: true },
    );

    if (response.status === 200) {
      const token = response.data.split(" ")[1];
      localStorage.setItem("accessToken", token);
      setAccessToken(token);
      getMainPaymentAccount();
      navigate("/");
    } else if (
      response.status === 400 &&
      response.data === "비밀번호가 일치하지 않습니다."
    ) {
      alert("비밀번호가 일치하지 않습니다.");
      setEmail(null);
      setPassword(null);
    }
  };

  return (
    <div className="bg-primary-light w-full h-dvh flex flex-col justify-center items-center">
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
            onChange={inputEmail}
          />
        </div>
        <div className="flex items-center border-[1.5px] border-primary-light border-solid rounded-md px-2">
          <img src={keyIcon} alt="비밀번호 아이콘" className="w-6 mr-2" />
          <input
            type="password"
            name="password"
            id="password"
            placeholder="비밀번호"
            className="h-9 text-xs focus:outline-none"
            onChange={inputPassword}
          />
        </div>
        <button type="button" className="btn-md w-[75%]" onClick={onClickLogin}>
          로그인
        </button>
        <button
          type="button"
          className="btn-gray-md w-[75%]"
          onClick={() => {
            navigate("/join");
          }}
        >
          회원가입
        </button>
      </div>
    </div>
  );
}

export default LoginPage;
