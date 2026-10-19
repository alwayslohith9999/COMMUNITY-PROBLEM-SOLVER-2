import api from './axios';

export const adminApi = {
  getStats: () => api.get('/admin/stats'),
  getPendingApprovals: () => api.get('/admin/pending-approvals'),
  getAllProblems: () => api.get('/admin/problems'),
  approveUser: (id) => api.post(`/admin/users/${id}/approve`),
  rejectUser: (id) => api.post(`/admin/users/${id}/reject`),
};
