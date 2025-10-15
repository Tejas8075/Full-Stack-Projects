import axios from "axios";

export const addItem = async (item) => {
  return await axios.post(`${import.meta.env.VITE_API_URL}/api/v1.0/admin/items`, item, {
    headers: {'Authorization': `Bearer ${localStorage.getItem('token')}`}
  });
}

export const deleteItem = async (itemId) => {
  return await axios.delete(`${import.meta.env.VITE_API_URL}/api/v1.0/admin/items/${itemId}`, {
    headers: {'Authorization': `Bearer ${localStorage.getItem('token')}`}
  });
}

export const fetchItems = async () => {
  return await axios.get(`${import.meta.env.VITE_API_URL}/api/v1.0/items`);
};
