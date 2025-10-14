import React from 'react'
import "./notFound.css"
import { useNavigate } from 'react-router-dom'

const NotFound = () => {

  const navigate = useNavigate();

  return (
    <div className="not-found-container">
      <div className="not-found-content">
        <h1 className="not-found-title">404</h1>

        <h2 className="not-found-subtitle">Oops! Page not found</h2>

        <p className="not-found-message">
          The page you're looking doesn't exist or has been moved.
        </p>

        <button onClick={() => navigate('/')} className="not-found-button">
          Go To homepage
        </button>
      </div>
    </div>
  )
}

export default NotFound