import api from './axios';

export const commentApi = {
  getByProblem: (problemId) => api.get(`/problems/${problemId}/comments`),
  create: (problemId, data) => api.post(`/problems/${problemId}/comments`, data),
  markAsSolution: (commentId) => api.post(`/comments/${commentId}/solution`),
  upvote: (commentId) => api.post(`/comments/${commentId}/upvote`),
};
