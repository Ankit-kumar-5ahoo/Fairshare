import React, { createContext, useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api'; 


const AuthContext = createContext(null);


export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(() => localStorage.getItem('token') || null);
  const navigate = useNavigate();

  
  useEffect(() => {
    if (token) {
      
      localStorage.setItem('token', token);
      setUser({ email: '...' }); 
    } else {
      
      localStorage.removeItem('token');
      setUser(null);
    }
  }, [token]);

  

  const login = async (email, password) => {
    try {
      const response = await api.post('/auth/login', { email, password });
      setToken(response.data.token);
      navigate('/'); 
    } catch (error) {
      console.error('Login failed:', error);
      
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