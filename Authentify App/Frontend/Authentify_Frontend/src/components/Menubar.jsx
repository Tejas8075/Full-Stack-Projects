import React from 'react'
import {assests} from "../assests/assests.js"
import {useNavigate} from "react-router-dom"

const Menubar = () => {

  const navigate = useNavigate();

  return (
    <nav className='navbar bg-white px-5 py-4 d-flex justify-content-between align-items-center'>
      <div className="d-flex align-items-center gap-2">
        <img src={assests.logo_home} alt="logo" width={32} height={32} />
        <span className="fw-bold fs-4 text-dark">Authentify</span>
      </div>

      <div className="btn btn-outline-dark rounded-pill px-3" onClick={() => navigate("/login")}>
        Login <i className="bi bi-arrow-right ms-2"></i>
      </div>
    </nav>
  )
}

export default Menubar