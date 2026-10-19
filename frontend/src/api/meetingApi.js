import api from './axios';

export const meetingApi = {
  getAll: () => api.get('/meetings'),
  getUpcoming: () => api.get('/meetings/upcoming'),
  getLive: () => api.get('/meetings/live'),
  getMyMeetings: () => api.get('/meetings/my'),
  getById: (id) => api.get(`/meetings/${id}`),
  getByProblem: (problemId) => api.get(`/meetings/problem/${problemId}`),
  create: (data) => api.post('/meetings', data),
  join: (id) => api.post(`/meetings/${id}/join`),
  leave: (id) => api.post(`/meetings/${id}/leave`),
};
