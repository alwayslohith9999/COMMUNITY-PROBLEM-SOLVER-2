import { useEffect, useState } from 'react';
import { Plus, Search, X } from 'lucide-react';
import { problemApi } from '../api/problemApi';
import ProblemCard from '../components/ProblemCard';
import PostProblemModal from '../components/PostProblemModal';

const categories = ['All', 'PLUMBING', 'ELECTRICAL', 'STRUCTURAL', 'NOISE', 'SECURITY', 'PARKING', 'LIFT', 'COMMON_AREA', 'OTHER'];
const urgencies = ['All', 'CRITICAL', 'HIGH', 'MEDIUM', 'LOW'];
const statuses = ['All', 'OPEN', 'IN_PROGRESS', 'RESOLVED', 'ESCALATED'];
const blocks = ['All Blocks', 'A', 'B', 'C', 'D', 'E'];
const sortOptions = [
  { value: 'latest', label: 'Latest' },
  { value: 'mostVoted', label: 'Most Voted' },
  { value: 'oldest', label: 'Oldest' },
];

export default function ProblemBoard() {
  const [problems, setProblems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [filters, setFilters] = useState({
    category: '', urgency: '', status: '', block: '', search: '', sort: 'latest'
  });

  const fetchProblems = async () => {
    setLoading(true);
    try {
      const params = {};
      if (filters.category && filters.category !== 'All') params.category = filters.category;
      if (filters.urgency && filters.urgency !== 'All') params.urgency = filters.urgency;
      if (filters.status && filters.status !== 'All') params.status = filters.status;
      if (filters.block && filters.block !== 'All Blocks') params.block = filters.block;
      if (filters.search) params.search = filters.search;
      params.sort = filters.sort;
      const response = await problemApi.getAll(params);
      setProblems(response.data);
    } catch (err) {
      console.error('Fetch problems error:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProblems();
  }, [filters.category, filters.urgency, filters.status, filters.block, filters.sort]);

  const handleSearch = (e) => {
    if (e.key === 'Enter') {
      fetchProblems();
    }
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-heading font-bold">Problem Board</h1>
        <button onClick={() => setShowModal(true)} className="btn-primary">
          <Plus size={18} /> Post Problem
        </button>
      </div>

      {/* Filters */}
      <div className="flex flex-wrap gap-3 items-center">
        <div className="relative">
          <Search size={16} className="absolute left-3 top-1/2 -translate-y-1/2 text-textMuted" />
          <input
            type="text"
            value={filters.search}
            onChange={(e) => setFilters({ ...filters, search: e.target.value })}
            onKeyDown={handleSearch}
            placeholder="Search problems..."
            className="h-9 pl-9 pr-8 bg-white border border-border rounded-lg text-sm focus:border-accent focus:ring-[3px] focus:ring-accent/15 outline-none w-[260px]"
          />
          {filters.search && (
            <button onClick={() => setFilters({ ...filters, search: '' })} className="absolute right-2 top-1/2 -translate-y-1/2 text-textMuted">
              <X size={14} />
            </button>
          )}
        </div>

        <select value={filters.category} onChange={(e) => setFilters({ ...filters, category: e.target.value })}
          className="h-9 px-3 bg-white border border-border rounded-lg text-sm focus:border-accent outline-none">
          {categories.map(c => <option key={c} value={c === 'All' ? '' : c}>{c.replace('_', ' ')}</option>)}
        </select>

        <select value={filters.urgency} onChange={(e) => setFilters({ ...filters, urgency: e.target.value })}
          className="h-9 px-3 bg-white border border-border rounded-lg text-sm focus:border-accent outline-none">
          {urgencies.map(u => <option key={u} value={u === 'All' ? '' : u}>{u}</option>)}
        </select>

        <select value={filters.status} onChange={(e) => setFilters({ ...filters, status: e.target.value })}
          className="h-9 px-3 bg-white border border-border rounded-lg text-sm focus:border-accent outline-none">
          {statuses.map(s => <option key={s} value={s === 'All' ? '' : s}>{s.replace('_', ' ')}</option>)}
        </select>

        <select value={filters.block} onChange={(e) => setFilters({ ...filters, block: e.target.value })}
          className="h-9 px-3 bg-white border border-border rounded-lg text-sm focus:border-accent outline-none">
          {blocks.map(b => <option key={b} value={b === 'All Blocks' ? '' : b}>{b === 'All Blocks' ? b : `Block ${b}`}</option>)}
        </select>

        <select value={filters.sort} onChange={(e) => setFilters({ ...filters, sort: e.target.value })}
          className="h-9 px-3 bg-white border border-border rounded-lg text-sm focus:border-accent outline-none">
          {sortOptions.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
        </select>
      </div>

      {/* Problem Grid */}
      {loading ? (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
          {[1,2,3,4].map(i => (
            <div key={i} className="card animate-pulse h-48">
              <div className="h-4 bg-gray-200 rounded w-1/4 mb-3" />
              <div className="h-5 bg-gray-200 rounded w-3/4 mb-2" />
              <div className="h-4 bg-gray-200 rounded w-full mb-2" />
              <div className="h-4 bg-gray-200 rounded w-2/3" />
            </div>
          ))}
        </div>
      ) : (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
          {problems.map(problem => (
            <ProblemCard key={problem.id} problem={problem} />
          ))}
        </div>
      )}

      {problems.length === 0 && !loading && (
        <div className="text-center py-16">
          <div className="text-6xl mb-4">&#128221;</div>
          <h3 className="text-lg font-medium text-textMain mb-2">No problems found</h3>
          <p className="text-textMuted text-sm mb-4">Try adjusting your filters or be the first to report a problem.</p>
          <button onClick={() => setShowModal(true)} className="btn-primary">
            <Plus size={16} /> Post a Problem
          </button>
        </div>
      )}

      {/* Post Problem Modal */}
      {showModal && <PostProblemModal onClose={() => { setShowModal(false); fetchProblems(); }} />}
    </div>
  );
}
