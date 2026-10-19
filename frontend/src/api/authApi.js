import api from './axios';

export const authApi = {
  login: (email, password) => api.post('/auth/login', { email, password }),
  register: (data) => api.post('/auth/register', data),
  refreshToken: (refreshToken) => api.post('/auth/refresh', { refreshToken }),
  getCurrentUser: () => api.get('/auth/me'),
};
