export function formatNumberWithCommas(number: number) {
  if (!number) return "0";
  const parts = number.toString().split(".");
  parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");
  return parts.join(".");
}

export function parseNumberIntoInteger(string: string) {
  if (!string) return 0;
  return parseFloat(string.toString().replace(/,/g, ""));
}

export function extractDate(date: string) {
  // "YYYY.MM.DD"
  const dt = new Date(date);
  const year = dt.getFullYear();
  const month = (dt.getMonth() + 1).toString().padStart(2, "0");
  const day = dt.getDate().toString().padStart(2, "0");
  return `${year}.${month}.${day}`;
}

export function extractTime(date: string) {
  // "HH:MM:SS"
  const dt = new Date(date);
  const hours = dt.getHours().toString().padStart(2, "0");
  const minutes = dt.getMinutes().toString().padStart(2, "0");
  const seconds = dt.getSeconds().toString().padStart(2, "0");
  return `${hours}:${minutes}:${seconds}`;
}
