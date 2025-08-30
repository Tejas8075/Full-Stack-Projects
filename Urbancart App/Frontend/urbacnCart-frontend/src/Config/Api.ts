// import axios from 'axios';

// export const API_URL = "http://localhost:5454";
// export const DEPLOYED_URL = "https://zosh-bazzar-backend.onrender.com"
// // change api

// export const api = axios.create({
//   baseURL: API_URL, 
//   headers: {
//     'Content-Type': 'application/json',
//   },
// });

import axios from "axios";

export const API_URL = "http://localhost:5454";
export const DEPLOYED_URL = "https://zosh-bazzar-backend.onrender.com";

// Create axios instance
export const api = axios.create({
  baseURL: API_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// 🔹 Interceptor: attach JWT automatically if present
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("jwt"); // store only raw token (eyJhb...)
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
