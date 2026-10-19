import api from './axios';

export const notificationApi = {
  getAll: () => api.get('/notifications'),
  getUnread: () => api.get('/notifications/unread'),
  countUnread: () => api.get('/notifications/count'),
  markAllRead: () => api.post('/notifications/read-all'),
  markAsRead: (id) => api.post(`/notifications/${id}/read`),
};
