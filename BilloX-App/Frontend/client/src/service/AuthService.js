import axios from "axios";

export const login = async (data) => {
  return await axios.post(`${import.meta.env.VITE_API_URL}/api/v1.0/login`, data);
}