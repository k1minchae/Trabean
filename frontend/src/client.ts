import axios, { AxiosInstance } from "axios";
// import useAuthStore from "./store/useAuthStore";

const client = (): AxiosInstance => {
  const ENDPOINT = process.env.REACT_APP_END_POINT;
  // const token = useAuthStore.getState().accessToken;
  const token =
    "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ5aHloIiwiaWF0IjoxNzI4MDU0NDA2LCJleHAiOjE3MjgwNTYyMDYsInN1YiI6InRlc3R0ZXN0dGVzdDFAc3NhZnkuY29tIiwiZW1haWwiOiJ0ZXN0dGVzdHRlc3QxQHNzYWZ5LmNvbSIsInVzZXJJZCI6MjUsInVzZXJuYW1lIjoi7Ket66as64KYIiwidXNlcktleSI6IjgwOGEyOTY4LTllMzAtNDNhMy1iNmRkLWJlZmY4NGQwMDZlMiJ9.G6AZSR1U_LSZZrKPlE7kRIcdwk6rpkJFnjEFrr8E_OE";

  if (!ENDPOINT) {
    throw new Error("Endpoint is not exist");
  }

  const config: {
    baseURL: string;
    headers: {
      "Content-Type": string;
      Authorization?: string;
    };
  } = {
    baseURL: ENDPOINT,
    headers: {
      "Content-Type": `application/json;charset=UTF-8`,
    },
  };

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return axios.create(config);
};

export default client;
