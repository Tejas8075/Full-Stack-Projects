import React from 'react'
import './manageUsers.css'
import UserForm from '../../components/UserForm/UserForm'
import UserList from '../../components/UserList/UserList'

export const ManageUsers = () => {
  return (
    <div className='users-container text-light'>
      <div className="left-column">
        {/* user-form */}
        <UserForm />
      </div>
      <div className="right-column">
        {/* list of users */}
        <UserList />
      </div>
    </div>
  )
}
