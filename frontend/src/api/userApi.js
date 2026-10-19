import api from './axios';

export const userApi = {
  getAll: () => api.get('/users'),
  getLeaderboard: () => api.get('/users/leaderboard'),
  getByBlock: (block) => api.get(`/users/block/${block}`),
  getProfile: (id) => api.get(`/users/${id}`),
  getPending: () => api.get('/users/pending'),
  approve: (id) => api.post(`/users/${id}/approve`),
  reject: (id) => api.post(`/users/${id}/reject`),
};
