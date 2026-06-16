import axiosInstance from './axios';

const authService = {
  register: (data) => {
    return axiosInstance.post('/auth/register', data);
  },

  login: (data) => {
    return axiosInstance.post('/auth/login', data);
  },

  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },

  forgotPassword: (data) => {
    return axiosInstance.post('/auth/forgot-password', data);
  },

  validateToken: (token) => {
    return axiosInstance.post(`/auth/validate-token/${token}`);
  },

  resetPassword: (data) => {
    return axiosInstance.post('/auth/reset-password', data);
  },

  getCurrentUser: () => {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  },

  getToken: () => {
    return localStorage.getItem('token');
  },

  isAuthenticated: () => {
    return !!localStorage.getItem('token');
  },
};

export default authService;
