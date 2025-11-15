import React from 'react';
import { useAuth } from '../context/AuthContext';
import { Navigate, Outlet } from 'react-router-dom';
import Navbar from './Navbar'; // <-- IMPORT THE NAVBAR

const ProtectedRoute = () => {
  const { token } = useAuth();

  // THIS IS YOUR "HACK" - UNCOMMENT THESE LINES TO ENABLE SECURITY
  // if (!token) {
  //   return <Navigate to="/login" replace />;
  // }

  // If logged in, show the layout
  return (
    <div>
      <Navbar /> {/* <-- RENDER NAVBAR */}
      <main>
        <Outlet /> {/* <-- Renders the current page (e.g., Dashboard) */}
      </main>
    </div>
  );
};

export default ProtectedRoute;