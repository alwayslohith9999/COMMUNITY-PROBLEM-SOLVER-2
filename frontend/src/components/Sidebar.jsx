import { NavLink, useLocation } from 'react-router-dom';
import {
  LayoutDashboard, ClipboardList, CalendarDays, Users,
  Megaphone, Trophy, UserCircle, Settings, LogOut,
  ShieldCheck, ChevronLeft, ChevronRight
} from 'lucide-react';
import { useAuthStore } from '../store/authStore';
import { useState } from 'react';

const mainNav = [
  { to: '/', icon: LayoutDashboard, label: 'Dashboard' },
  { to: '/problems', icon: ClipboardList, label: 'Problem Board' },
  { to: '/meetings', icon: CalendarDays, label: 'Meetings' },
  { to: '/directory', icon: Users, label: 'Directory' },
];

const communityNav = [
  { to: '/announcements', icon: Megaphone, label: 'Announcements' },
  { to: '/leaderboard', icon: Trophy, label: 'Leaderboard' },
];

const accountNav = [
  { to: '/profile', icon: UserCircle, label: 'Profile' },
  { to: '/settings', icon: Settings, label: 'Settings' },
];

export default function Sidebar() {
  const location = useLocation();
  const { user, logout, isAdmin } = useAuthStore();
  const [collapsed, setCollapsed] = useState(false);

  const isActive = (path) => {
    if (path === '/') return location.pathname === '/';
    return location.pathname.startsWith(path);
  };

  const renderNavItem = (item) => (
    <NavLink
      key={item.to}
      to={item.to}
      className={`sidebar-item ${isActive(item.to) ? 'active' : ''} ${collapsed ? 'justify-center' : ''}`}
      title={collapsed ? item.label : ''}
    >
      <item.icon size={20} />
      {!collapsed && <span>{item.label}</span>}
    </NavLink>
  );

  return (
    <aside className={`fixed left-0 top-0 h-screen bg-primary flex flex-col transition-all duration-200 z-40 ${collapsed ? 'w-16' : 'w-sidebar'}`}>
      {/* Logo */}
      <div className="h-header flex items-center px-4 border-b border-white/10">
        <div className="flex items-center gap-2">
          <div className="w-8 h-8 bg-accent rounded-lg flex items-center justify-center flex-shrink-0">
            <span className="text-primary font-bold text-sm">CF</span>
          </div>
          {!collapsed && (
            <span className="text-white font-heading font-semibold text-lg">CommunityFix</span>
          )}
        </div>
        <button
          onClick={() => setCollapsed(!collapsed)}
          className="ml-auto text-white/60 hover:text-white transition-colors"
        >
          {collapsed ? <ChevronRight size={16} /> : <ChevronLeft size={16} />}
        </button>
      </div>

      {/* Navigation */}
      <nav className="flex-1 overflow-y-auto py-4 px-2 space-y-6">
        {/* Main */}
        <div>
          {!collapsed && <div className="px-3 mb-2 text-[10px] font-medium text-white/40 uppercase tracking-wider">Main</div>}
          <div className="space-y-1">
            {mainNav.map(renderNavItem)}
          </div>
        </div>

        {/* Community */}
        <div>
          {!collapsed && <div className="px-3 mb-2 text-[10px] font-medium text-white/40 uppercase tracking-wider">Community</div>}
          <div className="space-y-1">
            {communityNav.map(renderNavItem)}
          </div>
        </div>

        {/* Admin */}
        {isAdmin() && (
          <div>
            {!collapsed && <div className="px-3 mb-2 text-[10px] font-medium text-white/40 uppercase tracking-wider">Admin</div>}
            <div className="space-y-1">
              <NavLink
                to="/admin"
                className={`sidebar-item ${isActive('/admin') ? 'active' : ''} ${collapsed ? 'justify-center' : ''}`}
              >
                <ShieldCheck size={20} />
                {!collapsed && <span>Admin Panel</span>}
              </NavLink>
            </div>
          </div>
        )}

        {/* Account */}
        <div>
          {!collapsed && <div className="px-3 mb-2 text-[10px] font-medium text-white/40 uppercase tracking-wider">Account</div>}
          <div className="space-y-1">
            {accountNav.map(renderNavItem)}
          </div>
        </div>
      </nav>

      {/* User & Logout */}
      <div className="border-t border-white/10 p-3">
        <button
          onClick={logout}
          className={`sidebar-item w-full text-danger/80 hover:text-danger hover:bg-danger/10 ${collapsed ? 'justify-center' : ''}`}
        >
          <LogOut size={20} />
          {!collapsed && <span>Logout</span>}
        </button>
        {!collapsed && user && (
          <div className="mt-3 flex items-center gap-2 px-3">
            <div className="w-8 h-8 rounded-full bg-accent/20 flex items-center justify-center flex-shrink-0">
              <span className="text-accent text-xs font-bold">
                {user.fullName?.split(' ').map(n => n[0]).join('').toUpperCase()}
              </span>
            </div>
            <div className="min-w-0">
              <p className="text-white text-xs font-medium truncate">{user.fullName}</p>
              <p className="text-white/50 text-[10px] truncate">{user.flatNumber}</p>
            </div>
          </div>
        )}
      </div>
    </aside>
  );
}
