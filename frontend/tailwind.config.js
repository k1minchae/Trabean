/** @type {import("tailwindcss").Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,jsx,ts,tsx}"],
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
