import React, { useContext } from 'react';
import { Navigate, Route, Routes, useLocation } from 'react-router-dom';
import Menubar from './components/Menubar/Menubar';
import { Toaster } from 'react-hot-toast';
import { AppContext } from './context/AppContext';

import Dashboard from './pages/Dashboard/Dashboard';
import Explore from './pages/Explore/Explore';
import ManageCategory from './pages/ManageCategory/ManageCategory';
import { ManageUsers } from './pages/ManageUsers/ManageUsers';
import { ManageItems } from './pages/ManageItems/ManageItems';
import Login from './pages/Login/Login';
import OrderHistory from './pages/OrderHistory/OrderHistory';
import NotFound from './pages/NotFound/NotFound';

const App = () => {
  const { auth } = useContext(AppContext);
  const location = useLocation();

  // Redirect logged-in users away from login page
  const LoginRoute = ({ element }) => {
    if (auth.token) return <Navigate to="/dashboard" replace />;
    return element;
  };

  // Protect routes, optional role-based access
  const ProtectedRoute = ({ element, allowedRoles }) => {
    if (!auth.token) return <Navigate to="/login" replace />;
    if (allowedRoles && !allowedRoles.includes(auth.role)) return <Navigate to="/dashboard" replace />;
    return element;
  };

  return (
    <div>
      {/* Hide menubar on login page */}
      {location.pathname !== '/login' && <Menubar />}

      <Toaster />

      <Routes>
        {/* Protected pages for all logged-in users */}
        <Route path='/' element={<ProtectedRoute element={<Dashboard />} />} />
        <Route path='/dashboard' element={<ProtectedRoute element={<Dashboard />} />} />
        <Route path='/explore' element={<ProtectedRoute element={<Explore />} />} />
        <Route path='/orders' element={<ProtectedRoute element={<OrderHistory />} />} />

        {/* Admin-only pages */}
        <Route path='/category' element={<ProtectedRoute element={<ManageCategory />} allowedRoles={['ROLE_ADMIN']} />} />
        <Route path='/users' element={<ProtectedRoute element={<ManageUsers />} allowedRoles={['ROLE_ADMIN']} />} />
        <Route path='/items' element={<ProtectedRoute element={<ManageItems />} allowedRoles={['ROLE_ADMIN']} />} />

        {/* Login page */}
        <Route path='/login' element={<LoginRoute element={<Login />} />} />

        {/* Catch all */}
        <Route path='*' element={<NotFound />} />
      </Routes>
    </div>
  );
};

export default App;
