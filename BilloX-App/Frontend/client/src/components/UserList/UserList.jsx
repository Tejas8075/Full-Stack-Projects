import React, { useState } from 'react'
import { deleteUser } from '../../service/UserService';
import toast from 'react-hot-toast';

const UserList = ({users, setUsers}) => {

  const [searchTerm, setSearchTerm] = useState("");

  const filteredUsers = users.filter(user => 
    user.name.toLowerCase().includes(searchTerm.toLowerCase())
  )

  const deleteByUserId = async (id) => {

    try {
      
      const response = await deleteUser(id);

      setUsers(prevUsers => prevUsers.filter(user => user.userId !== id));

      toast.success("User deleted");

    } catch (error) {
      console.error(error);
      toast.error("User not deleted");
    }

  }

  return (
    <div className='category-list-container'
      style={{ height: '100vh', overflowY: 'auto', overflowX: 'hidden' }}
    >
      <div className="row pe-2">
        {/* search bar */}
        <div className="input-group mb-3">
          <input
            type="text"
            id='keyword' placeholder='Search by keyword' className='form-control'
            onChange={(e) => setSearchTerm(e.target.value)}
            value={searchTerm}
          />

          <span className="input-group-text bg-warning">
            <i className="bi bi-search"></i>
          </span>
        </div>
      </div>

      <div className="row g-3 pe-2">
          {
            filteredUsers.map((user, index) => (
              <div key={index} className='col-12'>
                <div className="card p-3 bg-dark">
                  <div className="d-flex align-items-center">
                    <div className="flex-grow-1">
                      <h5 className='mb-1 text-white'>{user.name}</h5>

                      <p className="mb-0 text-white">{user.email}</p>
                    </div>

                    <div>
                      <button className='btn btn-danger btn-sm'
                      onClick={() => deleteByUserId(user.userId)}
                      >
                        <i className='bi bi-trash'></i>
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            ))
          }
      </div>
    </div>
  )
}

export default UserList