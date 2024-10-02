import React, { useState, useEffect } from "react";
import { PieChart, Pie, Cell, Legend } from "recharts";
import client from "../../client";
import { formatNumberWithCommas } from "../../utils/formatNumber";

interface Category {
  categoryName: string;
  amount: number;
  percent: number;
}

const PaymentHistory: React.FC = () => {
  const [token] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [showDate, setShowDate] = useState(false);
  const [isTextInput, setIsTextInput] = useState(true);
  const [totalAmount, setTotalAmount] = useState<string | null>(null);
  const [chartInfo, setCartInfo] = useState<Category[] | null>(null);

  const COLORS = ["#15803D", "#22C55E", "#86EFAC", "#BBF7D0", "#F0FDF4"];

  // 카테고리 이름 매핑 (영어 -> 한국어)
  const categoryNameMap: { [key: string]: string } = {
    FOOD: "음식",
    TRANSPORTATION: "교통",
    SHOPPING: "쇼핑",
    ACTIVITY: "활동",
    ACCOMMODATION: "숙박",
    OTHER: "기타",
  };

  // 날짜 선택
  const handleDateChange = (
    e: React.ChangeEvent<HTMLInputElement>,
    date: string,
  ) => {
    const newValue = e.target.value;

    if (date === "start") {
      // 시작일이 종료일보다 클 경우 시작일을 종료일로 설정
      if (endDate && new Date(newValue) > new Date(endDate)) {
        if (new Date(newValue) > new Date(endDate)) {
          setStartDate(endDate);
        }
        return;
      }
      const today = new Date();
      today.setHours(0, 0, 0, 0); // 시간 부분을 00:00:00으로 설정
      if (new Date(newValue) > today) {
        // 시작일이 오늘 날짜보다 미래일 경우, 시작일을 오늘 날짜로 설정
        const year = today.getFullYear().toString();
        const month = (today.getMonth() + 1).toString().padStart(2, "0"); // 월은 0부터 시작하므로 +1, 2자리로 맞춤
        const day = today.getDate().toString().padStart(2, "0"); // 날짜를 2자리로 맞춤
        const formattedDate = `${year}-${month}-${day}`; // 'YYYY-MM-DD' 형식으로 변환

        setStartDate(formattedDate);
        return;
      }
      setStartDate(newValue);
    } else if (date === "end") {
      const today = new Date();
      today.setHours(0, 0, 0, 0); // 시간 부분을 00:00:00으로 설정
      if (new Date(newValue) > today) {
        // 종료일이 오늘 날짜보다 미래일 경우, 종료일을 오늘 날짜로 설정
        const year = today.getFullYear().toString();
        const month = (today.getMonth() + 1).toString().padStart(2, "0"); // 월은 0부터 시작하므로 +1, 2자리로 맞춤
        const day = today.getDate().toString().padStart(2, "0"); // 날짜를 2자리로 맞춤
        const formattedDate = `${year}-${month}-${day}`; // 'YYYY-MM-DD' 형식으로 변환

        setEndDate(formattedDate);
        return;
      }
      setEndDate(newValue);
    }
  };

  // 날짜 선택 토글
  const toggleDate = () => {
    if (showDate) {
      setStartDate("");
      setEndDate("");
      setShowDate(false);
      setIsTextInput(true);
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

  const handleFetchChart = async () => {
    const params = {
      startdate: startDate ? formatDate(startDate) : null,
      enddate: endDate ? formatDate(endDate) : null,
    };
    const response = await client(token).get(`/api/payments/1/chart`, {
      params,
    });
    console.log(response.data);
    const price = response.data.totalAmount;
    const formattedPrice = formatNumberWithCommas(price);
    setTotalAmount(formattedPrice);
    setCartInfo(response.data.category);
    console.log(chartInfo);
  };

  useEffect(() => {
    handleFetchChart();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [token]);

  // 커스텀 레이블 렌더링 함수
  const renderCustomizedLabel = ({
    cx,
    cy,
    midAngle,
    innerRadius,
    outerRadius,
    percent,
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
  }: any) => {
    const RADIAN = Math.PI / 180;
    const radius = innerRadius + (outerRadius - innerRadius) * 0.35; // 값 조정: 0.5에서 0.35로 변경
    const x = Number(cx) + radius * Math.cos(-midAngle * RADIAN);
    const y = Number(cy) + radius * Math.sin(-midAngle * RADIAN);

    return (
      <text
        x={x}
        y={y}
        fill="white"
        textAnchor={x > cx ? "start" : "end"}
        dominantBaseline="central"
        fontSize="14px"
        fontWeight="bold"
      >
        {`${(percent * 100).toFixed(0)}%`}
      </text>
    );
  };

  return (
    <div className="w-full bg-[#F4F4F5] h-full pt-[4.375rem] flex flex-col items-center">
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
          <div
            id="date-select"
            className="w-full bg-white rounded-[0.625rem] px-[1rem] mt-[1.25rem] flex flex-col justify-center items-center"
          >
            <div className="w-full flex mt-3">
              <input
                type="text"
                placeholder="조회 시작일"
                value={startDate}
                onFocus={(e) => {
                  e.target.type = "date";
                  setIsTextInput(false);
                }}
                onBlur={(e) => {
                  if (e.target.value === "") {
                    e.target.type = "text";
                  }
                }}
                onChange={(e) => handleDateChange(e, "start")}
                className="bg-transparent p-2 w-1/2 text-center focus:outline-none text-sm"
              />
              <span className="mx-1">~</span>
              <input
                type="text"
                placeholder="조회 종료일"
                value={endDate}
                onFocus={(e) => {
                  e.target.type = "date";
                  setIsTextInput(false);
                }}
                onBlur={(e) => {
                  if (e.target.value === "") {
                    e.target.type = "text";
                  }
                }}
                onChange={(e) => handleDateChange(e, "end")}
                className="bg-transparent p-2 w-1/2 text-center focus:outline-none text-sm"
              />
            </div>
            <button
              type="button"
              className="my-2 btn-light-md w-[90%] focus:cursor-pointer hover:btn-md"
              onClick={() => handleFetchChart()}
            >
              조회
            </button>
            {isTextInput && (
              <p className="text-xs text-green-800 w-full my-2 text-center mb-3 mt-3">
                지정하지 않으면 모든 결제 기록을 확인할 수 있어요!
              </p>
            )}
          </div>
        )}

        <div className="w-full bg-white rounded-[0.625rem] px-[1rem] my-[1.25rem]">
          <p className="font-semibold text-gray-700 text-sm h-[2.5rem] flex items-center">
            <span className="w-[60%]">총 지출액</span>{" "}
            <span className="w-[60%]">{totalAmount} ₩</span>
          </p>
        </div>

        {chartInfo && chartInfo.length > 0 && (
          <div
            id="chart"
            className="bg-white rounded-[15px] py-[0.875rem] px-[1rem] flex flex-col items-center"
          >
            <h2 className="text-center font-semibold text-lg my-2">
              카테고리 별 지출
            </h2>
            <PieChart width={250} height={250}>
              <Pie
                data={chartInfo.map((item) => ({
                  name: item.categoryName,
                  value: item.percent,
                }))}
                cx="50%"
                cy="50%"
                innerRadius={50} // 내부 반지름을 설정하여 도넛 모양으로 만듦
                outerRadius={100} // 외부 반지름 설정
                labelLine={false}
                label={renderCustomizedLabel}
                // label={({ value }) => `${value}%`}
                dataKey="value"
              >
                {chartInfo.map((chart, index) => (
                  <Cell
                    // eslint-disable-next-line react/no-array-index-key
                    key={`cell-${chart.categoryName}`}
                    fill={COLORS[index % COLORS.length]}
                  />
                ))}
              </Pie>
              <Legend
                verticalAlign="bottom"
                align="center"
                height={36}
                formatter={(value) => categoryNameMap[value] || value}
                wrapperStyle={{
                  padding: "10px",
                  width: "100%",
                  fontSize: "14px",
                  bottom: "0px",
                }}
              />
            </PieChart>
          </div>
        )}
      </div>
    </div>
  );
};

export default PaymentHistory;
