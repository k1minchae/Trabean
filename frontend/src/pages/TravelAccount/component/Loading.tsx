import React from "react";

const Loading: React.FC = () => (
  <div className="flex justify-center items-center h-full">
    <div className="w-12 h-12 border-4 border-green-500 border-t-transparent border-solid rounded-full animate-spin" />
  </div>
);

export default Loading;
