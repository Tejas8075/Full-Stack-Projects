import axios from "axios";

export const addUser = async (user) => {
  return await axios.post(`${import.meta.env.VITE_API_URL}/api/v1.0/admin/register`, user, {
    headers: {'Authorization': `Bearer ${localStorage.getItem('token')}`}
  });
}

export const deleteUser = async (id) => {
  return await axios.delete(`${import.meta.env.VITE_API_URL}/api/v1.0/admin/users/${id}`, {
    headers: {'Authorization': `Bearer ${localStorage.getItem('token')}`}
  });
}

export const fetchUsers = async () => {
  return await axios.get(`${import.meta.env.VITE_API_URL}/api/v1.0/admin/users`, {
    headers: {'Authorization': `Bearer ${localStorage.getItem('token')}`}
  });
}