import React from 'react';
import { useAuth } from '../context/AuthContext';
import { Navigate, Outlet } from 'react-router-dom';
import Navbar from './Navbar'; 

const ProtectedRoute = () => {
  const { token } = useAuth();

  
  if (!token) {
     return <Navigate to="/login" replace />;
   }

  
  return (
    <div>
      <Navbar /> {}
      <main>
        <Outlet /> {}
      </main>
    </div>
  );
};

export default ProtectedRoute;