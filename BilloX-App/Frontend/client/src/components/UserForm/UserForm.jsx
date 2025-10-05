import React from 'react'

const UserForm = () => {
  return (
    <div className='mx-2 mt-2'>

      <div className="row">

        <div className="card col-md-8 form-container">
          <div className="card-body">
            <form>
              <div className="mb-3">
                <label htmlFor="name" className='form-label'>Name</label>

                <input type="text" name='name'
                  id='name'
                  className='form-control'
                  placeholder='John Doe'
                />
              </div>

              <div className="mb-3">
                <label htmlFor="email" className='form-label'>Email</label>

                <input type="email" name='email'
                  id='email'
                  className='form-control'
                  placeholder='yourname@example.com'
                />
              </div>

              <div className="mb-3">
                <label htmlFor="password" className='form-label'>Password</label>

                <input type="password" name='password'
                  id='password'
                  className='form-control'
                  placeholder='*******'
                />
              </div>

              <button className="btn btn-warning w-100" type='submit'>Save</button>
            </form>
          </div>
        </div>

      </div>

    </div>
  )
}

export default UserForm