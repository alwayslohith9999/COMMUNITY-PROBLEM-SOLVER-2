import { useEffect, useState } from 'react';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';
import Sidebar from './Sidebar';
import Header from './Header';
import MobileNav from './MobileNav';
import { useAuthStore } from '../store/authStore';
import { useNotificationStore } from '../store/notificationStore';
import { notificationApi } from '../api/notificationApi';

const pageTitles = {
  '/': 'Dashboard',
  '/problems': 'Problem Board',
  '/meetings': 'Community Meetings',
  '/directory': 'Resident Directory',
  '/announcements': 'Announcements',
  '/leaderboard': 'Leaderboard',
  '/profile': 'My Profile',
  '/settings': 'Settings',
  '/admin': 'Admin Panel',
};

export default function Layout() {
  const location = useLocation();
  const navigate = useNavigate();
  const { isAuthenticated } = useAuthStore();
  const [title, setTitle] = useState('Dashboard');

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    const path = Object.keys(pageTitles).find(p => location.pathname.startsWith(p)) || '/';
    setTitle(pageTitles[path] || 'CommunityFix');
  }, [location.pathname, isAuthenticated, navigate]);

  useEffect(() => {
    if (isAuthenticated) {
      notificationApi.getAll().then(res => {
        useNotificationStore.setState({ notifications: res.data });
      });
      notificationApi.countUnread().then(res => {
        useNotificationStore.setState({ unreadCount: res.data.count });
      });
    }
  }, [isAuthenticated]);

  if (!isAuthenticated) return null;

  return (
    <div className="min-h-screen bg-surface">
      {/* Desktop Sidebar */}
      <div className="hidden lg:block">
        <Sidebar />
      </div>

      {/* Header */}
      <div className="hidden lg:block">
        <Header title={title} />
      </div>

      {/* Mobile Header */}
      <div className="lg:hidden fixed top-0 left-0 right-0 h-header bg-white border-b border-border z-30 flex items-center px-4">
        <div className="flex items-center gap-2">
          <div className="w-8 h-8 bg-accent rounded-lg flex items-center justify-center">
            <span className="text-primary font-bold text-sm">CF</span>
          </div>
          <span className="font-heading font-semibold text-primary">CommunityFix</span>
        </div>
        <div className="ml-auto flex items-center gap-3">
          <button className="p-2 relative">
            <span className="text-textMain">&#128276;</span>
          </button>
        </div>
      </div>

      {/* Main Content */}
      <main className="lg:ml-sidebar lg:pt-header pb-tabbar lg:pb-0 min-h-screen">
        <div className="max-w-content mx-auto p-6 lg:p-8 pt-20 lg:pt-8">
          <Outlet />
        </div>
      </main>

      {/* Mobile Bottom Nav */}
      <div className="lg:hidden">
        <MobileNav />
      </div>
    </div>
  );
}
