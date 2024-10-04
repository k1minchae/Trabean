import React, { useState, useEffect, useCallback } from "react";
import client from "../../client";
import {
  formatNumberWithCommas,
  extractDate,
  extractTime,
} from "../../utils/formatNumber";
import accommodation from "../../assets/paymentListIcon/ACCOMMODATION.png";
import activity from "../../assets/paymentListIcon/ACTIVITY.png";
import food from "../../assets/paymentListIcon/FOOD.png";
import other from "../../assets/paymentListIcon/OTHER.png";
import shopping from "../../assets/paymentListIcon/SHOPPING.png";
import transportation from "../../assets/paymentListIcon/TRANSPORTATION.png";

interface ListProps {
  token: string | null;
  startDate: string | null;
  endDate: string | null;
  formatDate: (date: string) => string;
  travelAccountId: number;
  categoryName: string;
  handleTotalAmount: (amount: string) => void;
}

interface PaymentItem {
  payId: number;
  currency: string;
  merchantName: string;
  paymentDate: string;
  krwAmount: number | null;
  foreignAmount: number | null;
  category: string;
}

const CategoryList: React.FC<ListProps> = ({
  startDate,
  endDate,
  token,
  formatDate,
  travelAccountId,
  categoryName,
  handleTotalAmount,
}) => {
  const [payments, setPayments] = useState<PaymentItem[]>([]);
  const [page, setPage] = useState<number>(1);
  const [totalPage, setTotalPage] = useState<number | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  // 화폐 이름 매핑
  const currencyNameMap: { [key: string]: string } = {
    CAD: " $ (CAD)",
    CHF: " ₣ (CHF)",
    CNY: " ¥ (CNY)",
    EUR: " € (EUR)",
    GBP: " £ (GBP)",
    JPY: " ¥ (JPY)",
    USD: " $ (USD)",
  };

  // 카테고리 아이콘 매핑
  const categoryIconMap: { [key: string]: string } = {
    ACCOMMODATION: accommodation,
    ACTIVITY: activity,
    FOOD: food,
    OTHER: other,
    SHOPPING: shopping,
    TRANSPORTATION: transportation,
  };

  const fetchPaymentList = useCallback(
    async (reset = false) => {
      if (isLoading || (totalPage !== null && page > totalPage)) return;

      try {
        setIsLoading(true);
        const params = {
          startdate: startDate ? formatDate(startDate) : null,
          enddate: endDate ? formatDate(endDate) : null,
          page: reset ? 1 : page,
        };
        const response = await client(token || "").get(
          `/api/payments/${travelAccountId}/${categoryName}`,
          {
            params,
          },
        );

        if (reset) {
          setPayments(response.data.payments);
        } else {
          setPayments((prev) => [...prev, ...response.data.payments]);
        }
        console.log(page);
        setPage(response.data.pagination[0].currentPage + 1);
        setTotalPage(response.data.pagination[0].totalPages);
        handleTotalAmount(
          formatNumberWithCommas(response.data.categoryTotalAmount),
        );
      } catch (error) {
        console.error("Error fetching payment category list:", error);
      } finally {
        setIsLoading(false);
      }
    },
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [
      token,
      page,
      totalPage,
      startDate,
      endDate,
      formatDate,
      isLoading,
      travelAccountId,
      categoryName,
    ],
  );

  // 초기 렌더링 및 날짜 변경 시 데이터를 다시 불러오기 위한 useEffect 훅
  useEffect(() => {
    setPage(1);
    setTotalPage(null);
    setPayments([]);
    fetchPaymentList(true);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [token, startDate, endDate, travelAccountId, categoryName]);

  // 스크롤 이벤트를 감지하여 무한 스크롤 구현
  useEffect(() => {
    const handleScroll = () => {
      if (
        window.innerHeight + window.scrollY >=
        document.documentElement.scrollHeight - 100
      ) {
        fetchPaymentList(false);
      }
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [fetchPaymentList]);

  // 그룹화 (YYYY.MM.DD)
  const groupPaymentsByDate = (paymentList: PaymentItem[]) =>
    paymentList.reduce((groups: { [key: string]: PaymentItem[] }, payment) => {
      const date = extractDate(payment.paymentDate);
      if (!groups[date]) {
        // eslint-disable-next-line no-param-reassign
        groups[date] = [];
      }
      groups[date].push(payment);
      return groups;
    }, {});

  // 그룹화된 결제 내역
  const groupedPayments = groupPaymentsByDate(payments);

  // 가게명 7글자 초과 X
  const maxLength = 7;

  return (
    <div className="w-full bg-white">
      {payments && Object.keys(groupedPayments).length > 0
        ? Object.entries(groupedPayments).map(([date, paymentsOnDate]) => (
            <div key={date} className="w-full px-[20px] mb-4">
              {/* 날짜 */}
              <div className="border-b border-gray-300 pb-2 my-4 mt-6">
                <h5 className="text-base font-semibold">{date}</h5>
              </div>

              {/* 결제 내역 */}
              {paymentsOnDate.map((payment) => (
                <div
                  key={payment.payId}
                  className="w-full flex mb-4 items-center"
                >
                  <img
                    src={categoryIconMap[payment.category] || other}
                    alt={payment.category}
                    className="w-8 h-8 mr-2"
                  />
                  <div className="w-full">
                    <p className="text-xs text-gray-500 w-full flex justify-between">
                      <span>{extractTime(payment.paymentDate)}</span>
                      {payment.foreignAmount && (
                        <span className="text-green-700 font-semibold">
                          {formatNumberWithCommas(payment.foreignAmount)}
                          {currencyNameMap[payment.currency]}
                        </span>
                      )}
                    </p>
                    <p className="font-semibold flex justify-between w-full items-center">
                      <span className="">
                        {payment.merchantName.length > maxLength
                          ? `${payment.merchantName.substring(0, maxLength)}...`
                          : payment.merchantName}
                      </span>
                      <span className="text-sm text-gray-900">
                        {formatNumberWithCommas(payment.krwAmount || 0)} ₩
                      </span>
                    </p>
                  </div>
                </div>
              ))}
            </div>
          ))
        : null}

      {/* 결제내역 없을 때 */}
      {payments && payments.length === 0 && !isLoading ? (
        <p className="text-center my-5 text-gray-500">결제 내역이 없습니다.</p>
      ) : null}

      {/* 로딩중일 때 */}
      {isLoading && <div className="text-center">Loading...</div>}
    </div>
  );
};

export default CategoryList;
