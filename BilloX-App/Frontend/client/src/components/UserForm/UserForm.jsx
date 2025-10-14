import React, { useState } from 'react'
import { addUser } from '../../service/UserService'
import toast from 'react-hot-toast'

const UserForm = ({ setUsers }) => {
  const [loading, setLoading] = useState(false)
  const [data, setData] = useState({
    name: '',
    email: '',
    password: '',
    role: 'ROLE_USER',
  })

  const onChangeHandler = (e) => {
    const { name, value } = e.target
    setData((prevData) => ({ ...prevData, [name]: value }))
  }

  const onSubmitHandler = async (e) => {
    e.preventDefault()
    setLoading(true)

    try {
      const response = await addUser(data)
      setUsers((prevUsers) => [...prevUsers, response.data])
      toast.success('User added')

      setData({
        name: '',
        email: '',
        password: '',
        role: 'ROLE_USER',
      })
    } catch (error) {
      console.error(error)
      toast.error('Error adding user')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className='mx-2 mt-2'>
      <div className='row'>
        <div className='card col-md-12 form-container'>
          <div className='card-body'>
            <form onSubmit={onSubmitHandler}>
              <div className='mb-3'>
                <label htmlFor='name' className='form-label'>
                  Name
                </label>
                <input
                  type='text'
                  name='name'
                  id='name'
                  className='form-control'
                  placeholder='John Doe'
                  onChange={onChangeHandler}
                  value={data.name}
                  required
                />
              </div>

              <div className='mb-3'>
                <label htmlFor='email' className='form-label'>
                  Email
                </label>
                <input
                  type='email'
                  name='email'
                  id='email'
                  className='form-control'
                  placeholder='yourname@example.com'
                  onChange={onChangeHandler}
                  value={data.email}
                  required
                />
              </div>

              <div className='mb-3'>
                <label htmlFor='password' className='form-label'>
                  Password
                </label>
                <input
                  type='password'
                  name='password'
                  id='password'
                  className='form-control'
                  placeholder='*******'
                  onChange={onChangeHandler}
                  value={data.password}
                  required
                />
              </div>

              <button className='btn btn-warning w-100' type='submit' disabled={loading}>
                {loading ? 'loading...' : 'Save'}
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  )
}

export default UserForm
