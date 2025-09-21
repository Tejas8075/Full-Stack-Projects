import React, { useContext, useState } from 'react'
import { assets } from '../assets/assets'
import { useNavigate } from 'react-router-dom'
import { AppContext } from '../context/AppContext';
import axios from 'axios';
import { toast } from 'react-toastify';

const Login = () => {

  const navigate = useNavigate();

  const [state, setState] = useState('Sign Up')

  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const { backendUrl, setIsLoggedIn, getUserData } = useContext(AppContext);

  const onSubmitHandler = async (event) => {

    try {
      event.preventDefault();

      // To send cookies
      axios.defaults.withCredentials = true;

      if (state === "Sign Up") {
        const { data } = await axios.post(backendUrl + "/api/auth/register", { name, email, password })

        if (data.success) {
          setIsLoggedIn(true);
          getUserData();
          navigate("/");
        } else {
          toast.error(data.message);
        }
      } else {
        const { data } = await axios.post(backendUrl + "/api/auth/login", { email, password })

        if (data.success) {
          setIsLoggedIn(true);
          getUserData();
          navigate("/");
        } else {
          toast.error(data.message);
        }
      }
    } catch (error) {
        toast.error(error.message);
    }

  }

  return (
    <div className='flex flex-col justify-center items-center min-h-screen px-6 sm:px-0 bg-gradient-to-br from-blue-200 to-purple-400 relative'>

      {/* Logo */}
      <img
        src={assets.logo}
        alt="logo"
        className='absolute left-5 sm:left-20 top-5 w-28 sm:w-32 cursor-pointer'
        onClick={() => navigate("/")}
      />

      {/* Card container */}
      <div className='bg-white rounded-2xl shadow-lg p-8 sm:p-12 w-full max-w-md flex flex-col gap-6 items-center'>

        {/* Heading */}
        <h2 className='text-2xl font-semibold'>
          {state === 'Sign Up' ? "Create Account" : "Login"}
        </h2>

        {/* Subtext */}
        <p className='text-gray-500 text-center'>
          {state === 'Sign Up' ? "Create your account" : "Login to your account!"}
        </p>

        {/* Form */}
        <form className='w-full flex flex-col gap-4' onSubmit={onSubmitHandler}>

          {/* Name / Email field */}
          {state === 'Sign Up' && (
            <div className='flex items-center gap-3 border border-gray-300 rounded-full px-4 py-2'>
              <img src={assets.person_icon} alt="person" className='w-5 h-5' />
              <input
                type="text"
                placeholder='Full Name'
                className='w-full outline-none'
                required
                onChange={e => setName(e.target.value)}
                value={name}
              />
            </div>
          )}

          {/* Email field */}
          <div className='flex items-center gap-3 border border-gray-300 rounded-full px-4 py-2'>
            <img src={assets.mail_icon} alt="email" className='w-5 h-5' />
            <input
              type="email"
              placeholder='Email'
              className='w-full outline-none'
              required
              onChange={e => setEmail(e.target.value)}
              value={email}
            />
          </div>

          {/* Password field */}
          <div className='flex items-center gap-3 border border-gray-300 rounded-full px-4 py-2'>
            <img src={assets.lock_icon} alt="password" className='w-5 h-5' />
            <input
              type="password"
              placeholder='Password'
              className='w-full outline-none'
              required
              onChange={e => setPassword(e.target.value)}
              value={password}
            />
          </div>

          {/* Submit button */}
          <button
            type="submit"
            className='bg-blue-500 text-white py-2 rounded-full w-full hover:bg-blue-600 transition-all'
          >
            {state === 'Sign Up' ? "Sign Up" : "Login"}
          </button>

        </form>

        {/* Toggle & Forgot Password */}
        <div className='w-full flex justify-between items-center text-sm'>
          {/* Toggle Sign Up / Login */}
          <p
            className='text-blue-500 cursor-pointer hover:underline'
            onClick={() => setState(state === 'Sign Up' ? 'Login' : 'Sign Up')}
          >
            {state === 'Sign Up' ? "Already have an account? Login" : "Don't have an account? Sign Up"}
          </p>

          {/* Forgot Password (only show on Login) */}
          {state === 'Login' && (
            <p className='text-blue-500 cursor-pointer hover:underline' onClick={() => navigate("/reset-password")}>
              Forgot Password?
            </p>
          )}
        </div>


      </div>
    </div>
  )
}

export default Login
