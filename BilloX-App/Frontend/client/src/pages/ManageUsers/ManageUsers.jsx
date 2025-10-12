import React, { useEffect, useState } from 'react'
import './manageUsers.css'
import UserForm from '../../components/UserForm/UserForm'
import UserList from '../../components/UserList/UserList'
import toast from 'react-hot-toast'
import { fetchUsers } from '../../service/UserService'

export const ManageUsers = () => {

  const [users, setUsers] = useState([]);

  const [loading, setLoading] = useState(false);

  useEffect(() => {
    async function loadUsers() {
      try {
        setLoading(true);

        const response = await fetchUsers();

        setUsers(response.data);
      } catch (error) {
        console.error(error);
        toast.error("Unable to fetch users");
      } finally {
        setLoading(false);
      }
    }

    loadUsers();
  }, []);

  return (
    <div className='users-container text-light'>
      <div className="left-column">
        {/* user-form */}
        <UserForm setUsers={setUsers} />
      </div>
      <div className="right-column">
        {/* list of users */}
        <UserList users={users} setUsers={setUsers} />
      </div>
    </div>
  )
}
