import { useEffect, useState } from 'react';
import {
  Users, ClipboardList, CheckCircle, AlertTriangle, Clock,
  TrendingUp, BarChart3, Check, X
} from 'lucide-react';
import { adminApi } from '../api/adminApi';
import { userApi } from '../api/userApi';
import { problemApi } from '../api/problemApi';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, LineChart, Line } from 'recharts';

export default function Admin() {
  const [stats, setStats] = useState(null);
  const [pendingUsers, setPendingUsers] = useState([]);
  const [problems, setProblems] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [statsRes, pendingRes, problemsRes] = await Promise.all([
          adminApi.getStats(),
          adminApi.getPendingApprovals(),
          adminApi.getAllProblems(),
        ]);
        setStats(statsRes.data);
        setPendingUsers(pendingRes.data);
        setProblems(problemsRes.data);
      } catch (err) {
        console.error('Admin fetch error:', err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const handleApprove = async (id) => {
    try {
      await userApi.approve(id);
      setPendingUsers(pendingUsers.filter(u => u.id !== id));
    } catch (err) {
      console.error(err);
    }
  };

  const handleReject = async (id) => {
    try {
      await userApi.reject(id);
      setPendingUsers(pendingUsers.filter(u => u.id !== id));
    } catch (err) {
      console.error(err);
    }
  };

  const categoryData = [
    { name: 'Plumbing', count: problems.filter(p => p.category === 'PLUMBING').length },
    { name: 'Electrical', count: problems.filter(p => p.category === 'ELECTRICAL').length },
    { name: 'Structural', count: problems.filter(p => p.category === 'STRUCTURAL').length },
    { name: 'Noise', count: problems.filter(p => p.category === 'NOISE').length },
    { name: 'Security', count: problems.filter(p => p.category === 'SECURITY').length },
    { name: 'Parking', count: problems.filter(p => p.category === 'PARKING').length },
    { name: 'Lift', count: problems.filter(p => p.category === 'LIFT').length },
    { name: 'Common', count: problems.filter(p => p.category === 'COMMON_AREA').length },
  ];

  if (loading) {
    return (
      <div className="space-y-6">
        <div className="grid grid-cols-2 lg:grid-cols-5 gap-4">
          {[1,2,3,4,5].map(i => <div key={i} className="card h-24 animate-pulse" />)}
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-heading font-bold">Admin Dashboard</h1>

      {/* Stats */}
      <div className="grid grid-cols-2 lg:grid-cols-5 gap-4">
        <div className="card">
          <div className="flex items-center justify-between mb-2">
            <Users size={18} className="text-primary" />
          </div>
          <p className="text-2xl font-bold">{stats?.totalResidents || 0}</p>
          <p className="text-xs text-textMuted">Residents</p>
        </div>
        <div className="card">
          <div className="flex items-center justify-between mb-2">
            <Clock size={18} className="text-warning" />
          </div>
          <p className="text-2xl font-bold text-warning">{stats?.pendingApprovals || 0}</p>
          <p className="text-xs text-textMuted">Pending Approvals</p>
        </div>
        <div className="card">
          <div className="flex items-center justify-between mb-2">
            <ClipboardList size={18} className="text-primary-light" />
          </div>
          <p className="text-2xl font-bold">{stats?.openProblems || 0}</p>
          <p className="text-xs text-textMuted">Open Problems</p>
        </div>
        <div className="card">
          <div className="flex items-center justify-between mb-2">
            <CheckCircle size={18} className="text-success" />
          </div>
          <p className="text-2xl font-bold text-success">{stats?.resolvedThisMonth || 0}</p>
          <p className="text-xs text-textMuted">Resolved This Month</p>
        </div>
        <div className="card">
          <div className="flex items-center justify-between mb-2">
            <TrendingUp size={18} className="text-accent" />
          </div>
          <p className="text-2xl font-bold text-accent">{stats?.averageResolutionTime?.toFixed(1) || 0}d</p>
          <p className="text-xs text-textMuted">Avg Resolution</p>
        </div>
      </div>

      {/* Charts */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="card">
          <h3 className="text-base font-heading font-semibold mb-4 flex items-center gap-2">
            <BarChart3 size={18} /> Problems by Category
          </h3>
          <ResponsiveContainer width="100%" height={250}>
            <BarChart data={categoryData}>
              <CartesianGrid strokeDasharray="3 3" stroke="#E2E8F0" />
              <XAxis dataKey="name" tick={{ fontSize: 12 }} />
              <YAxis tick={{ fontSize: 12 }} />
              <Tooltip />
              <Bar dataKey="count" fill="#1E3A5F" radius={[4, 4, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>

        <div className="card">
          <h3 className="text-base font-heading font-semibold mb-4 flex items-center gap-2">
            <TrendingUp size={18} /> Problem Trends
          </h3>
          <ResponsiveContainer width="100%" height={250}>
            <LineChart data={[
              { day: 'Mon', posted: 3, resolved: 1 },
              { day: 'Tue', posted: 5, resolved: 2 },
              { day: 'Wed', posted: 2, resolved: 3 },
              { day: 'Thu', posted: 7, resolved: 4 },
              { day: 'Fri', posted: 4, resolved: 3 },
              { day: 'Sat', posted: 1, resolved: 2 },
              { day: 'Sun', posted: 2, resolved: 1 },
            ]}>
              <CartesianGrid strokeDasharray="3 3" stroke="#E2E8F0" />
              <XAxis dataKey="day" tick={{ fontSize: 12 }} />
              <YAxis tick={{ fontSize: 12 }} />
              <Tooltip />
              <Line type="monotone" dataKey="posted" stroke="#1E3A5F" strokeWidth={2} name="Posted" />
              <Line type="monotone" dataKey="resolved" stroke="#10B981" strokeWidth={2} name="Resolved" />
            </LineChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* Pending Approvals */}
      <div className="card">
        <h3 className="text-lg font-heading font-semibold mb-4">Pending Approvals</h3>
        {pendingUsers.length === 0 ? (
          <div className="text-center py-8">
            <CheckCircle size={40} className="text-success mx-auto mb-3" />
            <p className="text-textMuted">All residents approved!</p>
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="border-b border-border text-left text-textMuted">
                  <th className="pb-3 font-medium">Name</th>
                  <th className="pb-3 font-medium">Email</th>
                  <th className="pb-3 font-medium">Flat</th>
                  <th className="pb-3 font-medium">Block</th>
                  <th className="pb-3 font-medium">Actions</th>
                </tr>
              </thead>
              <tbody>
                {pendingUsers.map(u => (
                  <tr key={u.id} className="border-b border-border last:border-0">
                    <td className="py-3 font-medium">{u.fullName}</td>
                    <td className="py-3 text-textMuted">{u.email}</td>
                    <td className="py-3">{u.flatNumber}</td>
                    <td className="py-3">{u.block}</td>
                    <td className="py-3">
                      <div className="flex gap-2">
                        <button onClick={() => handleApprove(u.id)}
                          className="p-1.5 rounded-lg bg-success/10 text-success hover:bg-success/20 transition-colors">
                          <Check size={16} />
                        </button>
                        <button onClick={() => handleReject(u.id)}
                          className="p-1.5 rounded-lg bg-danger/10 text-danger hover:bg-danger/20 transition-colors">
                          <X size={16} />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}
