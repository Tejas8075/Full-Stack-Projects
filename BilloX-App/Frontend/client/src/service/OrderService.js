import axios from "axios";

export const latestOrders = async () => {
  return await axios.get(`${import.meta.env.VITE_API_URL}/api/v1.0/orders/latest`, {
    headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
  });
}

export const createOrder = async (order) => {
  return await axios.post(`${import.meta.env.VITE_API_URL}/api/v1.0/orders`, order, {
    headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
  });
}

export const deleteOrder = async (orderId) => {
  return await axios.get(`${import.meta.env.VITE_API_URL}/api/v1.0/orders/${orderId}`, {
    headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
  });
};
