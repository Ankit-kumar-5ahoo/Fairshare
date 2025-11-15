import React, { createContext, useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api'; // Import our new api.js

// 1. Create the Context
const AuthContext = createContext(null);

// 2. Create the Provider (a component that wraps our app)
export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(() => localStorage.getItem('token') || null);
  const navigate = useNavigate();

  // This effect runs when the 'token' state changes
  useEffect(() => {
    if (token) {
      // If we have a token, save it to localStorage
      localStorage.setItem('token', token);
      // We could also fetch user data here, e.g., /api/dashboard/me
      // For now, we'll just set a placeholder
      setUser({ email: '...' }); // We'll improve this later
    } else {
      // If token is null (e.g., on logout), remove it
      localStorage.removeItem('token');
      setUser(null);
    }
  }, [token]);

  // --- Functions ---

  const login = async (email, password) => {
    try {
      const response = await api.post('/auth/login', { email, password });
      setToken(response.data.token);
      navigate('/'); // Redirect to dashboard
    } catch (error) {
      console.error('Login failed:', error);
      // We'll handle showing this error to the user later
    }
  };

  const register = async (name, email, password) => {
    try {
      const response = await api.post('/auth/register', { name, email, password });
      setToken(response.data.token);
      navigate('/'); // Redirect to dashboard
    } catch (error) {
      console.error('Registration failed:', error);
    }
  };

  const logout = () => {
    setToken(null);
    navigate('/login'); // Redirect to login
  };

  // 3. The "value" is what all child components can access
  const value = {
    user,
    token,
    login,
    register,
    logout,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

// 4. A helper "hook" to easily use the context
export const useAuth = () => {
  return useContext(AuthContext);
};