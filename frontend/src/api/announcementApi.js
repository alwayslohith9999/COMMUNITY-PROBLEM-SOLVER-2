import api from './axios';

export const announcementApi = {
  getAll: () => api.get('/announcements'),
  getPinned: () => api.get('/announcements/pinned'),
  getById: (id) => api.get(`/announcements/${id}`),
  create: (data) => api.post('/announcements', data),
};
