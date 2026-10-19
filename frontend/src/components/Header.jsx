import { useState } from 'react';
import { Bell, Search, X } from 'lucide-react';
import { useNotificationStore } from '../store/notificationStore';
import { formatDistanceToNow } from 'date-fns';
import { useNavigate } from 'react-router-dom';

export default function Header({ title }) {
  const navigate = useNavigate();
  const { notifications, unreadCount, markAllRead } = useNotificationStore();
  const [showNotifications, setShowNotifications] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');

  const unreadNotifications = notifications.filter(n => !n.isRead);

  const getIconColor = (type) => {
    switch (type) {
      case 'COMMENT': return 'text-blue-500 bg-blue-50';
      case 'UPVOTE': return 'text-accent bg-accent/10';
      case 'MEETING': return 'text-success bg-success/10';
      case 'STATUS': return 'text-primary bg-primary/10';
      default: return 'text-gray-500 bg-gray-50';
    }
  };

  return (
    <header className="fixed top-0 right-0 left-0 lg:left-sidebar h-header bg-white border-b border-border z-30 flex items-center px-6">
      <div className="flex items-center justify-between w-full max-w-content mx-auto">
        {/* Title */}
        <h1 className="text-xl font-heading font-semibold text-textMain hidden lg:block">{title}</h1>

        {/* Search */}
        <div className="flex-1 max-w-md mx-8 hidden md:block">
          <div className="relative">
            <Search size={18} className="absolute left-3 top-1/2 -translate-y-1/2 text-textMuted" />
            <input
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="Search problems, residents..."
              className="w-full h-10 pl-10 pr-4 bg-gray-50 border border-border rounded-lg text-sm focus:border-accent focus:ring-[3px] focus:ring-accent/15 outline-none transition-all"
            />
            {searchQuery && (
              <button onClick={() => setSearchQuery('')} className="absolute right-3 top-1/2 -translate-y-1/2 text-textMuted hover:text-textMain">
                <X size={16} />
              </button>
            )}
          </div>
        </div>

        {/* Right Actions */}
        <div className="flex items-center gap-4 ml-auto">
          {/* Notifications */}
          <div className="relative">
            <button
              onClick={() => setShowNotifications(!showNotifications)}
              className="relative p-2 rounded-lg hover:bg-gray-50 transition-colors"
            >
              <Bell size={20} className="text-textMain" />
              {unreadCount > 0 && (
                <span className="absolute top-1 right-1 w-4 h-4 bg-danger text-white text-[10px] font-bold rounded-full flex items-center justify-center animate-pulse">
                  {unreadCount > 9 ? '9+' : unreadCount}
                </span>
              )}
            </button>

            {/* Notification Dropdown */}
            {showNotifications && (
              <>
                <div className="fixed inset-0 z-40" onClick={() => setShowNotifications(false)} />
                <div className="absolute right-0 top-full mt-2 w-[380px] max-h-[480px] bg-white rounded-xl shadow-lg border border-border overflow-hidden z-50">
                  <div className="flex items-center justify-between px-4 py-3 border-b border-border">
                    <h3 className="font-semibold text-sm">Notifications</h3>
                    <button onClick={markAllRead} className="text-xs text-primary hover:underline">
                      Mark all read
                    </button>
                  </div>
                  <div className="overflow-y-auto max-h-[400px]">
                    {unreadNotifications.length === 0 ? (
                      <div className="p-8 text-center text-textMuted text-sm">
                        No new notifications
                      </div>
                    ) : (
                      unreadNotifications.slice(0, 10).map((notification) => (
                        <button
                          key={notification.id}
                          onClick={() => {
                            if (notification.link) navigate(notification.link);
                            setShowNotifications(false);
                          }}
                          className="w-full flex items-start gap-3 px-4 py-3 hover:bg-gray-50 transition-colors text-left min-h-[48px] bg-blue-50/30"
                        >
                          <div className={`w-8 h-8 rounded-full flex items-center justify-center flex-shrink-0 ${getIconColor(notification.type)}`}>
                            <Bell size={14} />
                          </div>
                          <div className="flex-1 min-w-0">
                            <p className="text-sm text-textMain font-medium line-clamp-2">{notification.message}</p>
                            <p className="text-xs text-textMuted mt-0.5">
                              {notification.createdAt ? formatDistanceToNow(new Date(notification.createdAt), { addSuffix: true }) : 'Recently'}
                            </p>
                          </div>
                        </button>
                      ))
                    )}
                  </div>
                </div>
              </>
            )}
          </div>

          {/* Avatar */}
          <div className="w-9 h-9 rounded-full bg-primary flex items-center justify-center cursor-pointer">
            <span className="text-white text-xs font-bold">
              {useNotificationStore.getState().user?.fullName?.split(' ').map(n => n[0]).join('').toUpperCase() || 'U'}
            </span>
          </div>
        </div>
      </div>
    </header>
  );
}
