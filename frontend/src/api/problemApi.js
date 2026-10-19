import api from './axios';

export const problemApi = {
  getAll: (params) => api.get('/problems', { params }),
  getRecent: (limit = 5) => api.get('/problems/recent', { params: { limit } }),
  getById: (id) => api.get(`/problems/${id}`),
  create: (data) => api.post('/problems', data),
  updateStatus: (id, status) => api.put(`/problems/${id}/status`, { status }),
  assign: (id, volunteerId) => api.post(`/problems/${id}/assign/${volunteerId}`),
  toggleUpvote: (id) => api.post(`/problems/${id}/upvote`),
  toggleVolunteer: (id) => api.post(`/problems/${id}/volunteer`),
  delete: (id) => api.delete(`/problems/${id}`),
};
