import React, { useContext, useState } from 'react'
import assets from '../assets/assets'
import { useNavigate } from 'react-router-dom'
import { AuthContext } from '../context/AuthContext';

const LoginPage = () => {

  const navigate = useNavigate();

  const [currState, setCurrState] = useState("Sign up");

  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [bio, setBio] = useState("");
  const [isDataSubmitted, setIsDataSubmitted] = useState(false);

  const { login } = useContext(AuthContext);

  const onSubmitHandler = (event) => {
    event.preventDefault();

    if (currState === "Sign up" && !isDataSubmitted) {
      setIsDataSubmitted(true);
      return;
    }

    login(currState === "Sign up" ? "signup" : "login", { fullName, email, password, bio }, navigate);
  };


  return (
    <div className='min-h-screen bg-cover bg-center flex items-center justify-center gap-8 sm:justify-evenly max-sm:flex-col backdrop-blur-2xl'>

      {/* left img */}
      <img src={assets.logo_big} alt="logo" className='w-[min(30vw,250px)]' />

      {/* right img */}
      <form className='border-2 bg-white/8 text-white border-gray-500 p-6 flex flex-col gap-6 rounded-lg shadow-lg' onSubmit={onSubmitHandler}>
        <h2 className='font-medium text-2xl flex justify-between items-center'>
          {currState}
          {isDataSubmitted && <img onClick={() => setIsDataSubmitted(false)} src={assets.arrow_icon} alt="arrow" className='w-5 cursor-pointer' />}
        </h2>

        {currState === "Sign up" && !isDataSubmitted && (
          <input type="text" className='p-2 border border-gray-500 rounded-md focus:outline-none' placeholder='Full Name' required onChange={(e) => setFullName(e.target.value)} value={fullName} />
        )}

        {!isDataSubmitted && (
          <>
            <input type="email" className='p-2 border border-gray-500 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500' placeholder='Email Id' required onChange={(e) => setEmail(e.target.value)} value={email} />

            <input type="password" className='p-2 border border-gray-500 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500' placeholder='Password' required onChange={(e) => setPassword(e.target.value)} value={password} />
          </>
        )}

        {currState === "Sign up" && isDataSubmitted && (
          <textarea rows={4} className='p-2 border border-gray-500 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500' placeholder='provide a short bio...' required onChange={(e) => setBio(e.target.value)} value={bio} ></textarea>
        )}

        <button type='submit' className='py-3 bg-gradient-to-r from-purple-400 to-violet-600 text-white rounded-md cursor-pointer'>
          {currState === "Sign up" ? "Create Account" : "Login Now"}
        </button>

        <div className='flex items-center gap-2 text-sm text-gray-500'>
          <input type="checkbox" required />

          <p>Agree to the terms of use & privacy policy.</p>
        </div>

        <div className='flex flex-col gap-2'>
          {currState === "Sign up" ? (
            <p className='text-sm text-gray-600'>Alreay have an account? <span className='font-medium text-violet-500 cursor-pointer' onClick={() => { setCurrState("Login"); setIsDataSubmitted(false) }}>Login here</span></p>
          ) : (
            <p className='text-sm text-gray-600'>Create an account <span className='font-medium text-violet-500 cursor-pointer' onClick={() => { setCurrState("Sign up") }}>Click here</span></p>
          )}
        </div>
      </form>
    </div>
  )
}

export default LoginPage