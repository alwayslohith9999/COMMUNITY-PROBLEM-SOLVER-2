import { NavLink, useLocation } from 'react-router-dom';
import { Home, ClipboardList, CalendarDays, Bell, UserCircle } from 'lucide-react';

const tabs = [
  { to: '/', icon: Home, label: 'Home' },
  { to: '/problems', icon: ClipboardList, label: 'Problems' },
  { to: '/meetings', icon: CalendarDays, label: 'Meetings' },
  { to: '/announcements', icon: Bell, label: 'Alerts' },
  { to: '/profile', icon: UserCircle, label: 'Profile' },
];

export default function MobileNav() {
  const location = useLocation();

  return (
    <nav className="fixed bottom-0 left-0 right-0 h-tabbar bg-white border-t border-border z-50 flex items-center justify-around">
      {tabs.map((tab) => {
        const isActive = tab.to === '/' ? location.pathname === '/' : location.pathname.startsWith(tab.to);
        return (
          <NavLink
            key={tab.to}
            to={tab.to}
            className={`flex flex-col items-center gap-0.5 py-2 px-3 transition-colors ${
              isActive ? 'text-accent' : 'text-textMuted'
            }`}
          >
            <tab.icon size={24} />
            <span className="text-[10px] font-medium">{tab.label}</span>
          </NavLink>
        );
      })}
    </nav>
  );
}
