import React from "react";

interface TargetAmountProgressBarProps {
  targetAmount: number;
  collectedAmount: number;
}

const TargetAmountProgressBar: React.FC<TargetAmountProgressBarProps> = ({
  targetAmount,
  collectedAmount,
}) => {
  // 퍼센트 계산 (목표 금액 대비 모인 금액)
  const percentage = Math.min((collectedAmount / targetAmount) * 100, 100);

  return (
    <div className="w-full bg-gray-200 h-4">
      <div className="bg-primary h-4" style={{ width: `${percentage}%` }} />
    </div>
  );
};

export default TargetAmountProgressBar;
