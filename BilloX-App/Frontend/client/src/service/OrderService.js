import axios from "axios";

export const latestOrders = async () => {
  return await axios.get(`http://localhost:8080/api/v1.0/orders/latet`, {
    headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
  });
}

export const createOrder = async (order) => {
  return await axios.post(`http://localhost:8080/api/v1.0/orders`, order, {
    headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
  });
}

export const deleteOrder = async (orderId) => {
  return await axios.get(`http://localhost:8080/api/v1.0/orders/${orderId}`, {
    headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
  });
};
