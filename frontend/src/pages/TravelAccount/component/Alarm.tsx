import React from "react";

const Alarm: React.FC = () => {
  const handleAlarmClick = () => {
    alert("알림 누름!!!!!!");
  };

  return (
    <div className="text-right p-4">
      <button type="button" onClick={handleAlarmClick}>
        알림
      </button>
    </div>
  );
};

export default Alarm;
