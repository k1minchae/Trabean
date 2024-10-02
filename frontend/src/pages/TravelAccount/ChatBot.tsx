import React from "react";
import bean from "../../assets/bean.png";

const ChatBot: React.FC = () => {
  console.log("챗봇");
  return (
    <div>
      <div className="bg-red-500">
        <img className="w-10 h-10" src={bean} alt="이미지" />
      </div>
    </div>
  );
};

export default ChatBot;
