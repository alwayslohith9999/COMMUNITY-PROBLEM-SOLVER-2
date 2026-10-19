import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  ClipboardList, CheckCircle, AlertTriangle, CalendarDays,
  TrendingUp, ArrowRight, Plus, Megaphone
} from 'lucide-react';
import { problemApi } from '../api/problemApi';
import { announcementApi } from '../api/announcementApi';
import { meetingApi } from '../api/meetingApi';
import ProblemCard from '../components/ProblemCard';
import { useAuthStore } from '../store/authStore';

export default function Dashboard() {
  const navigate = useNavigate();
  const { user } = useAuthStore();
  const [stats, setStats] = useState({
    totalProblems: 0, resolvedToday: 0, criticalOpen: 0, activeMeetings: 0
  });
  const [recentProblems, setRecentProblems] = useState([]);
  const [announcements, setAnnouncements] = useState([]);
  const [meetings, setMeetings] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [problemsRes, announcementsRes, meetingsRes] = await Promise.all([
          problemApi.getRecent(5),
          announcementApi.getPinned(),
          meetingApi.getUpcoming(),
        ]);
        setRecentProblems(problemsRes.data);
        setAnnouncements(announcementsRes.data);
        setMeetings(meetingsRes.data.slice(0, 3));

        // Calculate stats
        const allProblems = await problemApi.getAll({});
        const problems = allProblems.data;
        setStats({
          totalProblems: problems.length,
          resolvedToday: problems.filter(p => p.status === 'RESOLVED').length,
          criticalOpen: problems.filter(p => p.urgency === 'CRITICAL' && p.status === 'OPEN').length,
          activeMeetings: meetingsRes.data.length,
        });
      } catch (err) {
        console.error('Dashboard fetch error:', err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  if (loading) {
    return (
      <div className="space-y-6">
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          {[1,2,3,4].map(i => (
            <div key={i} className="card animate-pulse">
              <div className="h-4 bg-gray-200 rounded w-1/2 mb-3" />
              <div className="h-8 bg-gray-200 rounded w-1/3" />
            </div>
          ))}
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Stats Row */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <div className="card">
          <div className="flex items-center justify-between mb-2">
            <span className="text-sm text-textMuted">Total Problems</span>
            <ClipboardList size={18} className="text-primary" />
          </div>
          <p className="text-3xl font-bold text-textMain">{stats.totalProblems}</p>
          <p className="text-xs text-textMuted mt-1">this month</p>
        </div>

        <div className="card">
          <div className="flex items-center justify-between mb-2">
            <span className="text-sm text-textMuted">Resolved</span>
            <CheckCircle size={18} className="text-success" />
          </div>
          <p className="text-3xl font-bold text-textMain">{stats.resolvedToday}</p>
          <div className="flex items-center gap-1 mt-1">
            <TrendingUp size={12} className="text-success" />
            <span className="text-xs text-success font-medium">12% vs last week</span>
          </div>
        </div>

        <div className="card border-l-4 border-l-danger">
          <div className="flex items-center justify-between mb-2">
            <span className="text-sm text-textMuted">Critical Open</span>
            <AlertTriangle size={18} className="text-danger" />
          </div>
          <p className="text-3xl font-bold text-danger">{stats.criticalOpen}</p>
          <p className="text-xs text-danger/70 mt-1">needs immediate attention</p>
        </div>

        <div className="card">
          <div className="flex items-center justify-between mb-2">
            <span className="text-sm text-textMuted">Active Meetings</span>
            <CalendarDays size={18} className="text-primary-light" />
          </div>
          <p className="text-3xl font-bold text-textMain">{stats.activeMeetings}</p>
          <p className="text-xs text-textMuted mt-1">upcoming this week</p>
        </div>
      </div>

      {/* Second Row */}
      <div className="grid grid-cols-1 lg:grid-cols-5 gap-6">
        {/* Recent Problems */}
        <div className="lg:col-span-3">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-lg font-heading font-semibold">Recent Problems</h2>
            <button onClick={() => navigate('/problems')} className="text-sm text-primary hover:underline flex items-center gap-1">
              View all <ArrowRight size={14} />
            </button>
          </div>
          <div className="space-y-3">
            {recentProblems.map(problem => (
              <ProblemCard key={problem.id} problem={problem} compact />
            ))}
          </div>
        </div>

        {/* Quick Actions + My Stats */}
        <div className="lg:col-span-2 space-y-6">
          {/* Quick Actions */}
          <div className="card">
            <h3 className="text-base font-heading font-semibold mb-4">Quick Actions</h3>
            <div className="space-y-2">
              <button onClick={() => navigate('/problems')} className="btn-primary w-full">
                <Plus size={16} /> Post a Problem
              </button>
              <button onClick={() => navigate('/meetings')} className="btn-outline w-full">
                <CalendarDays size={16} /> Schedule Meeting
              </button>
              <button onClick={() => navigate('/announcements')} className="btn-outline w-full">
                <Megaphone size={16} /> View Announcements
              </button>
            </div>
          </div>

          {/* My Stats */}
          <div className="card">
            <h3 className="text-base font-heading font-semibold mb-4">My Stats</h3>
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <span className="text-sm text-textMuted">Problems Posted</span>
                <span className="text-lg font-bold text-textMain">{user?.problemsPosted || 0}</span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-textMuted">Solutions Given</span>
                <span className="text-lg font-bold text-textMain">{user?.solutionsGiven || 0}</span>
              </div>
              <div className="flex items-center justify-between pt-3 border-t border-border">
                <span className="text-sm text-textMuted">Reputation</span>
                <div className="flex items-center gap-1">
                  <span className="text-lg font-bold text-accent">{user?.reputation || 0}</span>
                  <span className="text-accent">&#9733;</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Pinned Announcements */}
      {announcements.length > 0 && (
        <div className="space-y-3">
          <h2 className="text-lg font-heading font-semibold">Pinned Announcements</h2>
          {announcements.map(a => (
            <div key={a.id} className="card border-l-4 border-l-accent">
              <div className="flex items-start gap-3">
                <Megaphone size={20} className="text-accent flex-shrink-0 mt-0.5" />
                <div>
                  <h4 className="font-medium text-textMain">{a.title}</h4>
                  <p className="text-sm text-textMuted mt-1 line-clamp-2">{a.content}</p>
                  <p className="text-xs text-textMuted mt-2">By {a.postedBy?.fullName}</p>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
