/** @type {import("tailwindcss").Config} */
module.exports = {
  content: ["./index.html", "./src/**/*.{js,jsx,ts,tsx}"], // HTML 및 JS 파일 경로
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: "#6FA760",
          light: "#C0DEB8",
        },
        yellow: "#FEC72A",
      },
    },
    fontFamily: {
      Pretendard: ["Pretendard"],
    },
  },
  plugins: [],
};
