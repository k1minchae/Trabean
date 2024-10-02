import React from "react";

interface NavBarProps {
  text: string;
}

const NavBar: React.FC<NavBarProps> = ({ text }) => {
  console.log("네비게이션 바");
  return (
    <div>
      <div className="flex pb-8 bg-red-500">
        <div>←</div>
        <div className="flex-grow text-center">{text}</div>
      </div>
    </div>
  );
};

export default NavBar;
