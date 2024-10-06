import beanAdmin from "../../../assets/bean_admin.png";
import beanPayer from "../../../assets/bean_on.png";
import beanNonePayer from "../../../assets/bean_off.png";

import cad from "../../../assets/flagIcon/cad.png";
import chf from "../../../assets/flagIcon/chf.png";
import cny from "../../../assets/flagIcon/cny.png";
import eur from "../../../assets/flagIcon/eur.png";
import gbp from "../../../assets/flagIcon/gbp.png";
import jpy from "../../../assets/flagIcon/jpy.png";
import krw from "../../../assets/flagIcon/krw.png";
import usd from "../../../assets/flagIcon/usd.png";

const currencySymbols: { [key: string]: string } = {
  KRW: "₩", // 한국 원화
  USD: "$", // 미국 달러
  EUR: "€", // 유로
  JPY: "¥", // 일본 엔화
  CNY: "¥", // 중국 위안화
  GBP: "£", // 영국 파운드
  CHF: "₣", // 스위스 프랑
  CAD: "C$", // 캐나다 달러
};

export function getCurrencySymbol(currencyCode: string): string {
  return currencySymbols[currencyCode] || ""; // 없는 통화일 경우 빈 문자열 반환
}

export const getBeanImage = (role: string) => {
  switch (role) {
    case "PAYER":
      return beanPayer;
    case "NONE_PAYER":
      return beanNonePayer;
    default:
      return beanAdmin;
  }
};

export const formatUserRole = (role: string) => {
  switch (role) {
    case "ADMIN":
      return "모임장";
    case "PAYER":
      return "결제가능";
    case "NONE_PAYER":
      return "결제불가";
    default:
      return "";
  }
};

export const getCurrencyImage = (exchangeCurrency: string) => {
  switch (exchangeCurrency) {
    case "USD":
      return usd;
    case "EUR":
      return eur;
    case "GBP":
      return gbp;
    case "JPY":
      return jpy;
    case "CNY":
      return cny;
    case "KRW":
      return krw;
    case "CAD":
      return cad;
    default:
      return chf;
  }
};

// 가능한 통화 목록 정의
export const allCurrencies = [
  { country: "미국", exchangeCurrency: "USD" },
  { country: "유럽", exchangeCurrency: "EUR" },
  { country: "영국", exchangeCurrency: "GBP" },
  { country: "일본", exchangeCurrency: "JPY" },
  { country: "중국", exchangeCurrency: "CNY" },
  { country: "한국", exchangeCurrency: "KRW" },
  { country: "캐나다", exchangeCurrency: "CAD" },
  { country: "스위스", exchangeCurrency: "CHF" },
];

// 현재 날짜 구하는 함수
export const getToday = () => {
  const today = new Date();
  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, "0");
  const day = String(today.getDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
};

// 주어진 "YYYYMMDD" 형식의 문자열을 "YYYY-MM-DD"로 변환하는 함수
export const formatDateString = (dateString: string): string => {
  const year = dateString.substring(0, 4);
  const month = dateString.substring(4, 6);
  const day = dateString.substring(6, 8);
  return `${year}-${month}-${day}`;
};

// 통화에 따른 최소 금액 설정 함수
export const getMinimumAmount = (exchangeCurrency: string) => {
  switch (exchangeCurrency) {
    case "USD":
    case "EUR":
    case "JPY":
    case "CHF":
      return 100;
    case "CNY":
      return 800;
    case "GBP":
      return 80;
    case "CAD":
      return 140;
    default:
      return 0;
  }
};
