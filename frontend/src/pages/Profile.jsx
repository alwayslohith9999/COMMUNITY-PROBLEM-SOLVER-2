import { useEffect, useState } from 'react';
import { Star, ClipboardList, MessageSquare, CalendarDays, Settings } from 'lucide-react';
import { useAuthStore } from '../store/authStore';
import { problemApi } from '../api/problemApi';
import { commentApi } from '../api/commentApi';
import ProblemCard from '../components/ProblemCard';

export default function Profile() {
  const { user } = useAuthStore();
  const [activeTab, setActiveTab] = useState('problems');
  const [myProblems, setMyProblems] = useState([]);
  const [myComments, setMyComments] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [problemsRes, commentsRes] = await Promise.all([
          problemApi.getAll({}),
          commentApi.getMyComments(),
        ]);
        setMyProblems(problemsRes.data.filter(p => p.postedBy?.id === user?.id));
        setMyComments(commentsRes.data);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    if (user?.id) fetchData();
  }, [user]);

  const getInitials = (name) => {
    if (!name) return '?';
    const parts = name.split(' ');
    if (parts.length >= 2) return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
    return name.substring(0, 2).toUpperCase();
  };

  const getProgressToNext = () => {
    const rep = user?.reputation || 0;
    if (rep >= 500) return 100;
    if (rep >= 300) return ((rep - 300) / 200) * 100;
    if (rep >= 200) return ((rep - 200) / 100) * 100;
    if (rep >= 150) return ((rep - 150) / 50) * 100;
    if (rep >= 100) return ((rep - 100) / 50) * 100;
    return (rep / 100) * 100;
  };

  const tabs = [
    { key: 'problems', label: 'My Problems', icon: ClipboardList },
    { key: 'comments', label: 'My Suggestions', icon: MessageSquare },
    { key: 'meetings', label: 'Meetings', icon: CalendarDays },
    { key: 'settings', label: 'Settings', icon: Settings },
  ];

  return (
    <div className="space-y-6 max-w-4xl mx-auto">
      {/* Profile Header */}
      <div className="card">
        <div className="flex flex-col sm:flex-row items-center gap-6">
          <div className="w-24 h-24 rounded-full bg-primary flex items-center justify-center">
            <span className="text-white text-2xl font-bold">{getInitials(user?.fullName)}</span>
          </div>
          <div className="text-center sm:text-left flex-1">
            <h1 className="text-2xl font-heading font-bold">{user?.fullName}</h1>
            <p className="text-textMuted mt-1">{user?.flatNumber} · Block {user?.block}</p>
            <p className="text-xs text-textMuted mt-1">Member since {user?.createdAt ? new Date(user.createdAt).toLocaleDateString() : 'Recently'}</p>
          </div>
          <div className="text-center">
            <div className="flex items-center gap-2 justify-center">
              <Star size={24} className="text-accent fill-accent" />
              <span className="text-3xl font-bold text-accent">{user?.reputation || 0}</span>
            </div>
            <span className="badge bg-accent/10 text-primary text-xs mt-2">{user?.reputationRank || 'New Member'}</span>
            <div className="mt-3 w-32 h-2 bg-gray-100 rounded-full overflow-hidden">
              <div className="h-full bg-accent rounded-full transition-all" style={{ width: `${getProgressToNext()}%` }} />
            </div>
          </div>
        </div>
      </div>

      {/* Tabs */}
      <div className="flex gap-1 border-b border-border overflow-x-auto">
        {tabs.map(tab => (
          <button
            key={tab.key}
            onClick={() => setActiveTab(tab.key)}
            className={`flex items-center gap-2 px-4 py-3 text-sm font-medium whitespace-nowrap transition-colors ${
              activeTab === tab.key
                ? 'text-primary border-b-2 border-primary'
                : 'text-textMuted hover:text-textMain'
            }`}
          >
            <tab.icon size={16} />
            {tab.label}
          </button>
        ))}
      </div>

      {/* Tab Content */}
      {activeTab === 'problems' && (
        <div className="space-y-4">
          {loading ? (
            <div className="space-y-3">
              {[1,2].map(i => <div key={i} className="card h-32 animate-pulse" />)}
            </div>
          ) : myProblems.length > 0 ? (
            myProblems.map(p => <ProblemCard key={p.id} problem={p} />)
          ) : (
            <div className="text-center py-12">
              <ClipboardList size={40} className="text-textMuted mx-auto mb-3" />
              <p className="text-textMuted">You haven&apos;t posted any problems yet.</p>
            </div>
          )}
        </div>
      )}

      {activeTab === 'comments' && (
        <div className="space-y-3">
          {myComments.map(comment => (
            <div key={comment.id} className="card">
              <p className="text-sm text-textMain">{comment.content}</p>
              <p className="text-xs text-textMuted mt-2">On: {comment.problemTitle || 'Problem'}</p>
            </div>
          ))}
          {myComments.length === 0 && (
            <div className="text-center py-12">
              <MessageSquare size={40} className="text-textMuted mx-auto mb-3" />
              <p className="text-textMuted">No suggestions yet.</p>
            </div>
          )}
        </div>
      )}

      {activeTab === 'meetings' && (
        <div className="text-center py-12">
          <CalendarDays size={40} className="text-textMuted mx-auto mb-3" />
          <p className="text-textMuted">Meeting history coming soon.</p>
        </div>
      )}

      {activeTab === 'settings' && (
        <div className="card max-w-lg">
          <h3 className="text-lg font-heading font-semibold mb-4">Settings</h3>
          <div className="space-y-4">
            <div>
              <label className="input-label">Full Name</label>
              <input type="text" defaultValue={user?.fullName} className="input-field" readOnly />
            </div>
            <div>
              <label className="input-label">Email</label>
              <input type="email" defaultValue={user?.email} className="input-field bg-gray-50" readOnly />
              <p className="text-xs text-textMuted mt-1">Email cannot be changed</p>
            </div>
            <div>
              <label className="input-label">Phone Number</label>
              <input type="tel" defaultValue={user?.phoneNumber} className="input-field" />
            </div>
            <button className="btn-primary">Save Changes</button>
          </div>
        </div>
      )}
    </div>
  );
}
