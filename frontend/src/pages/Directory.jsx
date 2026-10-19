import { useEffect, useState } from 'react';
import { Search, MapPin, Star, Users } from 'lucide-react';
import { userApi } from '../api/userApi';

export default function Directory() {
  const [residents, setResidents] = useState([]);
  const [filtered, setFiltered] = useState([]);
  const [search, setSearch] = useState('');
  const [blockFilter, setBlockFilter] = useState('All');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    userApi.getAll().then(res => {
      setResidents(res.data);
      setFiltered(res.data);
      setLoading(false);
    }).catch(err => {
      console.error(err);
      setLoading(false);
    });
  }, []);

  useEffect(() => {
    let result = residents;
    if (blockFilter !== 'All') {
      result = result.filter(r => r.block === blockFilter);
    }
    if (search) {
      const q = search.toLowerCase();
      result = result.filter(r =>
        r.fullName?.toLowerCase().includes(q) ||
        r.flatNumber?.toLowerCase().includes(q) ||
        r.block?.toLowerCase().includes(q)
      );
    }
    setFiltered(result);
  }, [search, blockFilter, residents]);

  const getInitials = (name) => {
    if (!name) return '?';
    const parts = name.split(' ');
    if (parts.length >= 2) return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
    return name.substring(0, 2).toUpperCase();
  };

  const blocks = ['All', 'A', 'B', 'C', 'D', 'E'];

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-heading font-bold">Resident Directory</h1>

      {/* Filters */}
      <div className="flex flex-wrap gap-3">
        <div className="relative flex-1 max-w-sm">
          <Search size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-textMuted" />
          <input
            type="text"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            placeholder="Search residents..."
            className="w-full h-10 pl-10 pr-4 bg-white border border-border rounded-lg text-sm focus:border-accent focus:ring-[3px] focus:ring-accent/15 outline-none"
          />
        </div>
        <div className="flex gap-2">
          {blocks.map(b => (
            <button
              key={b}
              onClick={() => setBlockFilter(b)}
              className={`h-10 px-4 rounded-lg text-sm font-medium transition-colors ${
                blockFilter === b
                  ? 'bg-primary text-white'
                  : 'bg-white border border-border text-textMuted hover:bg-gray-50'
              }`}
            >
              {b === 'All' ? 'All Blocks' : `Block ${b}`}
            </button>
          ))}
        </div>
      </div>

      {/* Grid */}
      {loading ? (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {[1,2,3,4,5,6].map(i => (
            <div key={i} className="card animate-pulse h-32">
              <div className="flex items-center gap-3">
                <div className="w-12 h-12 rounded-full bg-gray-200" />
                <div className="space-y-2">
                  <div className="h-4 bg-gray-200 rounded w-24" />
                  <div className="h-3 bg-gray-200 rounded w-16" />
                </div>
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {filtered.map(resident => (
            <div key={resident.id} className="card group relative overflow-hidden">
              <div className="flex items-center gap-4">
                <div className="w-14 h-14 rounded-full bg-primary flex items-center justify-center flex-shrink-0">
                  <span className="text-white text-lg font-bold">{getInitials(resident.fullName)}</span>
                </div>
                <div className="flex-1 min-w-0">
                  <h3 className="font-semibold text-textMain truncate">{resident.fullName}</h3>
                  <div className="flex items-center gap-2 mt-1">
                    <MapPin size={12} className="text-textMuted" />
                    <span className="text-sm text-textMuted">{resident.flatNumber}</span>
                    <span className="badge bg-primary/10 text-primary text-[10px]">Block {resident.block}</span>
                  </div>
                </div>
              </div>
              <div className="flex items-center justify-between mt-4 pt-3 border-t border-border">
                <div className="flex items-center gap-1">
                  <Star size={14} className="text-accent fill-accent" />
                  <span className="text-sm font-medium">{resident.reputation || 0}</span>
                  <span className="text-xs text-textMuted ml-1">{resident.reputationRank}</span>
                </div>
                <span className="text-xs text-textMuted">{resident.email}</span>
              </div>
            </div>
          ))}
        </div>
      )}

      {filtered.length === 0 && !loading && (
        <div className="text-center py-16">
          <Users size={48} className="text-textMuted mx-auto mb-4" />
          <p className="text-textMuted">No residents found matching your search.</p>
        </div>
      )}
    </div>
  );
}
