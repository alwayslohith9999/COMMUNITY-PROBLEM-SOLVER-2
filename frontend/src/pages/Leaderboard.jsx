import { useEffect, useState } from 'react';
import { Trophy, Star, Medal } from 'lucide-react';
import { userApi } from '../api/userApi';

const rankColors = {
  'Community Legend': 'text-purple-600 bg-purple-50',
  'Problem Solver': 'text-primary bg-blue-50',
  'Active Helper': 'text-success bg-green-50',
  'Rising Star': 'text-accent bg-amber-50',
  'Contributor': 'text-textMuted bg-gray-50',
};

export default function Leaderboard() {
  const [leaders, setLeaders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    userApi.getLeaderboard().then(res => {
      setLeaders(res.data);
      setLoading(false);
    }).catch(err => {
      console.error(err);
      setLoading(false);
    });
  }, []);

  const getInitials = (name) => {
    if (!name) return '?';
    const parts = name.split(' ');
    if (parts.length >= 2) return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
    return name.substring(0, 2).toUpperCase();
  };

  const getRankIcon = (index) => {
    if (index === 0) return <Trophy size={18} className="text-amber-500" />;
    if (index === 1) return <Medal size={18} className="text-gray-400" />;
    if (index === 2) return <Medal size={18} className="text-amber-700" />;
    return <span className="text-sm font-bold text-textMuted w-[18px] text-center">{index + 1}</span>;
  };

  return (
    <div className="space-y-6 max-w-3xl mx-auto">
      <div className="text-center mb-8">
        <Trophy size={40} className="text-accent mx-auto mb-3" />
        <h1 className="text-2xl font-heading font-bold">Reputation Leaderboard</h1>
        <p className="text-textMuted text-sm mt-1">Top contributors in the community</p>
      </div>

      {loading ? (
        <div className="space-y-3">
          {[1,2,3,4,5].map(i => <div key={i} className="card h-16 animate-pulse" />)}
        </div>
      ) : (
        <div className="space-y-3">
          {leaders.map((user, index) => (
            <div key={user.id} className={`card flex items-center gap-4 p-4 ${index < 3 ? 'border-l-4 border-l-accent' : ''}`}>
              <div className="w-8 flex justify-center">
                {getRankIcon(index)}
              </div>
              <div className="w-12 h-12 rounded-full bg-primary flex items-center justify-center flex-shrink-0">
                <span className="text-white font-bold">{getInitials(user.fullName)}</span>
              </div>
              <div className="flex-1 min-w-0">
                <h3 className="font-semibold text-textMain truncate">{user.fullName}</h3>
                <p className="text-sm text-textMuted">{user.flatNumber} · Block {user.block}</p>
              </div>
              <div className="text-right">
                <div className="flex items-center gap-1 justify-end">
                  <Star size={14} className="text-accent fill-accent" />
                  <span className="text-lg font-bold text-accent">{user.reputation}</span>
                </div>
                <span className={`badge ${rankColors[user.reputationRank] || 'text-textMuted bg-gray-50'} text-[10px] mt-1`}>
                  {user.reputationRank}
                </span>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
