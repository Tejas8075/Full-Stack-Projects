import React, { useContext } from 'react';
import Menubar from './components/Menubar/Menubar';
import { Navigate, Route, Routes, useLocation } from 'react-router-dom';
import Dashboard from './pages/Dashboard/Dashboard';
import ManageCategory from './pages/ManageCategory/ManageCategory';
import { ManageUsers } from './pages/ManageUsers/ManageUsers';
import { ManageItems } from './pages/ManageItems/ManageItems';
import Explore from './pages/Explore/Explore';
import { Toaster } from 'react-hot-toast';
import Login from './pages/Login/Login';
import OrderHistory from './pages/OrderHistory/OrderHistory';
import { AppContext } from './context/AppContext';
import NotFound from './pages/NotFound/NotFound';

const App = () => {
  const location = useLocation();
  const { auth } = useContext(AppContext);

  // Redirect logged-in users from Login page
  const RequireLogout = ({ children }) => {
    return auth.token ? <Navigate to="/dashboard" replace /> : children;
  };

  // Protected Route
  const ProtectedRoute = ({ children, allowedRoles }) => {
    if (!auth.token) return <Navigate to="/login" replace />;
    if (allowedRoles && !allowedRoles.includes(auth.role))
      return <Navigate to="/dashboard" replace />;
    return children;
  };

  return (
    <div>
      {location.pathname !== '/login' && <Menubar />}

      <Toaster />

      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/explore" element={<Explore />} />

        {/* Admin Only Routes */}
        <Route
          path="/category"
          element={
            <ProtectedRoute allowedRoles={['ROLE_ADMIN']}>
              <ManageCategory />
            </ProtectedRoute>
          }
        />
        <Route
          path="/users"
          element={
            <ProtectedRoute allowedRoles={['ROLE_ADMIN']}>
              <ManageUsers />
            </ProtectedRoute>
          }
        />
        <Route
          path="/items"
          element={
            <ProtectedRoute allowedRoles={['ROLE_ADMIN']}>
              <ManageItems />
            </ProtectedRoute>
          }
        />

        {/* Login Route */}
        <Route
          path="/login"
          element={
            <RequireLogout>
              <Login />
            </RequireLogout>
          }
        />

        {/* Orders Route */}
        <Route
          path="/orders"
          element={
            <ProtectedRoute allowedRoles={['ROLE_ADMIN', 'ROLE_USER']}>
              <OrderHistory />
            </ProtectedRoute>
          }
        />

        <Route path='*' element={<NotFound />} />
      </Routes>
    </div>
  );
};

export default App;
