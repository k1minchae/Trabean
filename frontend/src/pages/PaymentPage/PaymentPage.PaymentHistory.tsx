import React, { useState } from "react";
import Search from "./PaymentPage.PaymentHistory.Search";
import Chart from "./PaymentPage.PaymentHistory.Chart";
import List from "./PaymentPage.PaymentHistory.List";
import CategoryList from "./PaymentPage.PaymentHistory.CategoryList";
import TopBar from "../../components/TopBar";

const PaymentHistory: React.FC = () => {
  const [token] = useState<string | null>(null);
  const [startDate, setStartDate] = useState<string | null>(null);
  const [endDate, setEndDate] = useState<string | null>(null);
  const [showDate, setShowDate] = useState<boolean>(false);
  const [signalFetchChart, setSignalFetchChart] = useState<boolean>(false);
  const [totalAmount, setTotalAmount] = useState<string | null>(null);

  // 카테고리 선택
  const [category, setCategory] = useState<string>("ALL");

  // 날짜 선택 토글
  const toggleDate = () => {
    if (showDate) {
      setStartDate(null);
      setEndDate(null);
      setShowDate(false);
    } else if (!showDate) {
      setShowDate(true);
    }
  };

  const formatDate = (date: string) => {
    const year = new Date(date).getFullYear().toString().slice(2);
    const month = (new Date(date).getMonth() + 1).toString().padStart(2, "0");
    const day = new Date(date).getDate().toString().padStart(2, "0");
    const formattedDate = year + month + day;
    return formattedDate;
  };

  // props 함수들
  const handleStartDate = (date: string) => {
    setStartDate(date);
  };
  const handleEndDate = (date: string) => {
    setEndDate(date);
  };
  const handleTotalAmount = (amount: string) => {
    setTotalAmount(amount);
  };
  const handleFetchChart = () => {
    setSignalFetchChart(() => !signalFetchChart);
  };
  const handleCategory = (categoryName: string) => {
    setCategory(categoryName);
    console.log(category);
  };

  return (
    <>
      <TopBar isWhite={false} isLogo={false} page="가계부" />
      <div className="w-full bg-[#F4F4F5] h-vdh py-[4.375rem] flex flex-col items-center pt-[90px]">
        <div className="w-[300px]">
          <h1 className="text-base text-gray-900 font-semibold flex justify-between">
            지출 내역
            <button
              type="button"
              className="font-semibold focus:cursor-pointer"
              onClick={toggleDate}
            >
              {showDate ? "선택 취소" : "기간 선택"}{" "}
              <span className="text-xs mx-1">▼</span>
            </button>
          </h1>
          {showDate && (
            <Search
              handleStartDate={handleStartDate}
              handleEndDate={handleEndDate}
              startDate={startDate}
              endDate={endDate}
              handleFetchChart={handleFetchChart}
            />
          )}
          <div className="w-full bg-white rounded-[0.625rem] px-[1rem] my-[1.25rem]">
            <p className="font-semibold text-gray-700 text-sm h-[2.5rem] flex items-center">
              <span className="w-[60%]">총 지출액</span>{" "}
              <span className="w-[60%]">{totalAmount} ₩</span>
            </p>
          </div>
          <div className="bg-white rounded-[15px] py-[0.875rem] px-[1rem] flex flex-col items-center h-full">
            <Chart
              handleTotalAmount={handleTotalAmount}
              token={token}
              startDate={startDate}
              endDate={endDate}
              formatDate={formatDate}
              signalFetchChart={signalFetchChart}
              handleCategory={handleCategory}
            />
            {category === "ALL" && (
              <List
                startDate={startDate}
                endDate={endDate}
                token={token}
                formatDate={formatDate}
              />
            )}
            {category !== "ALL" && (
              <CategoryList
                startDate={startDate}
                endDate={endDate}
                token={token}
                formatDate={formatDate}
                categoryName={category}
                travelAccountId={1}
                handleTotalAmount={handleTotalAmount}
              />
            )}
          </div>
        </div>
      </div>
    </>
  );
};

export default PaymentHistory;
