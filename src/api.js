import axios from 'axios';

// This reads the URL from your .env file
const API_URL = import.meta.env.VITE_API_BASE_URL;

// Create a new 'instance' of axios with our backend's URL
const api = axios.create({
  baseURL: API_URL,
});

// This is the magic. This "interceptor" runs BEFORE every single request.
api.interceptors.request.use(
  (config) => {
    // It checks if we have a 'token' saved in the browser's storage
    const token = localStorage.getItem('token');

    if (token) {
      // If the token exists, add it to the 'Authorization' header
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    // Do something with request error
    return Promise.reject(error);
  }
);

export default api;