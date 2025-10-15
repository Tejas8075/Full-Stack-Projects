import axios from "axios";

export const fetchDashboardData = async () => {
  return await axios.get(`${import.meta.env.VITE_API_URL}/api/v1.0/dashboard`, {
    headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
  });
}