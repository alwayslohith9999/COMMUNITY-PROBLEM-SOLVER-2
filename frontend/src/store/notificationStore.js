import { create } from 'zustand';
import { notificationApi } from '../api/notificationApi';

export const useNotificationStore = create((set, get) => ({
  notifications: [],
  unreadCount: 0,
  isLoading: false,

  fetchNotifications: async () => {
    try {
      const response = await notificationApi.getAll();
      set({ notifications: response.data });
    } catch (error) {
      console.error('Failed to fetch notifications:', error);
    }
  },

  fetchUnreadCount: async () => {
    try {
      const response = await notificationApi.countUnread();
      set({ unreadCount: response.data.count });
    } catch (error) {
      console.error('Failed to fetch unread count:', error);
    }
  },

  markAllRead: async () => {
    try {
      await notificationApi.markAllRead();
      set({ unreadCount: 0, notifications: get().notifications.map(n => ({ ...n, isRead: true })) });
    } catch (error) {
      console.error('Failed to mark all read:', error);
    }
  },

  addNotification: (notification) => {
    set({
      notifications: [notification, ...get().notifications],
      unreadCount: get().unreadCount + 1,
    });
  },
}));
