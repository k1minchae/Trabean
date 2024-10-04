import React, { useState, useEffect } from "react";
import { PieChart, Pie, Cell, Legend } from "recharts";
import client from "../../client";
import { formatNumberWithCommas } from "../../utils/formatNumber";

interface ChartProps {
  startDate: string | null;
  endDate: string | null;
  token: string | null;
  formatDate: (date: string) => string;
  handleTotalAmount: (amount: string) => void;
  signalFetchChart: boolean;
  handleCategory: (category: string) => void;
}

interface Category {
  categoryName: string;
  amount: number;
  percent: number;
}

const Chart: React.FC<ChartProps> = ({
  startDate,
  endDate,
  token,
  formatDate,
  handleTotalAmount,
  signalFetchChart,
  handleCategory,
}) => {
  const [chartInfo, setCartInfo] = useState<Category[] | null>(null);

  const COLORS = [
    "#15803D",
    "#60A57A",
    "#22C55E",
    "#93BEA3",
    "#86EFAC",
    "#96AB9D",
  ];

  // 카테고리 이름 매핑 (영어 -> 한국어)
  const categoryNameMap: { [key: string]: string } = {
    ALL: "전체 항목",
    FOOD: "음식",
    TRANSPORTATION: "교통",
    SHOPPING: "쇼핑",
    ACTIVITY: "여가",
    ACCOMMODATION: "숙박",
    OTHER: "기타",
  };

  const handleCategoryChange = (
    event: React.ChangeEvent<HTMLSelectElement>,
  ) => {
    handleCategory(event.target.value);
  };

  const fetchChart = async () => {
    const params = {
      startdate: startDate ? formatDate(startDate) : null,
      enddate: endDate ? formatDate(endDate) : null,
    };
    const response = await client().get(`/api/payments/1/chart`, {
      params,
    });
    console.log(response.data);
    const price = response.data.totalAmount;
    const formattedPrice = formatNumberWithCommas(price);
    handleTotalAmount(formattedPrice);
    setCartInfo(response.data.category);
    console.log(chartInfo);
  };

  useEffect(() => {
    fetchChart();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [token, signalFetchChart]);

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

  // 여기부터 html
  return (
    <div>
      {chartInfo && chartInfo.length > 0 && (
        <div id="chart" className="flex flex-col items-center">
          <h2 className="text-center font-semibold text-lg my-2">
            항목별 결제 내역
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
              verticalAlign="top"
              align="center"
              height={36}
              formatter={(value) => categoryNameMap[value] || value}
              wrapperStyle={{
                padding: "10px",
                width: "100%",
                fontSize: "14px",
                top: "-14px",
              }}
            />
          </PieChart>

          {/* 카테고리 셀렉트 박스 */}
          <div className="mt-4 btn-outline-md w-full flex justify-center">
            <select
              className="focus:outline-none bg-transparent w-full text-center"
              onChange={handleCategoryChange}
            >
              {Object.entries(categoryNameMap).map(([key, value]) => (
                <option key={key} value={key}>
                  {value}{" "}
                </option>
              ))}
            </select>
          </div>
        </div>
      )}
    </div>
  );
};

export default Chart;
