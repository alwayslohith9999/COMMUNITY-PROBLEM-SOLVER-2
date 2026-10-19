import { ThumbsUp, MessageSquare, ArrowRight, HandHeart } from 'lucide-react';
import { formatDistanceToNow } from 'date-fns';
import { useNavigate } from 'react-router-dom';
import { problemApi } from '../api/problemApi';
import { useState } from 'react';

const urgencyColors = {
  CRITICAL: 'border-l-danger',
  HIGH: 'border-l-warning',
  MEDIUM: 'border-l-accent',
  LOW: 'border-l-success',
};

const urgencyBadgeColors = {
  CRITICAL: 'bg-danger text-white',
  HIGH: 'bg-warning text-white',
  MEDIUM: 'bg-accent text-primary',
  LOW: 'bg-success text-white',
};

const statusBadgeColors = {
  OPEN: 'bg-gray-100 text-gray-700',
  IN_PROGRESS: 'bg-blue-100 text-blue-700',
  RESOLVED: 'bg-success/10 text-success',
  ESCALATED: 'bg-danger/10 text-danger',
};

const categoryIcons = {
  PLUMBING: '&#128167;',
  ELECTRICAL: '&#9889;',
  STRUCTURAL: '&#127959;',
  NOISE: '&#128266;',
  SECURITY: '&#128274;',
  PARKING: '&#128663;',
  LIFT: '&#8633;',
  COMMON_AREA: '&#127793;',
  OTHER: '&#128203;',
};

export default function ProblemCard({ problem, compact }) {
  const navigate = useNavigate();
  const [upvoted, setUpvoted] = useState(problem.userHasUpvoted);
  const [upvoteCount, setUpvoteCount] = useState(problem.upvoteCount);
  const [volunteer, setVolunteer] = useState(problem.userIsVolunteer);

  const handleUpvote = async (e) => {
    e.stopPropagation();
    try {
      await problemApi.toggleUpvote(problem.id);
      setUpvoted(!upvoted);
      setUpvoteCount(upvoted ? upvoteCount - 1 : upvoteCount + 1);
    } catch (err) {
      console.error('Upvote failed:', err);
    }
  };

  const handleVolunteer = async (e) => {
    e.stopPropagation();
    try {
      await problemApi.toggleVolunteer(problem.id);
      setVolunteer(!volunteer);
    } catch (err) {
      console.error('Volunteer failed:', err);
    }
  };

  const getInitials = (name) => {
    if (!name) return '?';
    const parts = name.split(' ');
    if (parts.length >= 2) return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
    return name.substring(0, 2).toUpperCase();
  };

  if (compact) {
    return (
      <div
        onClick={() => navigate(`/problems/${problem.id}`)}
        className="flex items-center gap-4 p-4 bg-white border border-border rounded-xl hover:shadow-md transition-all cursor-pointer"
      >
        <div className="w-10 h-10 rounded-lg bg-gray-50 flex items-center justify-center text-lg flex-shrink-0"
          dangerouslySetInnerHTML={{ __html: categoryIcons[problem.category] || '&#128203;' }} />
        <div className="flex-1 min-w-0">
          <p className="text-sm font-medium text-textMain truncate">{problem.title}</p>
          <p className="text-xs text-textMuted">{problem.postedBy?.flatNumber} · {problem.block}</p>
        </div>
        <span className={`badge ${urgencyBadgeColors[problem.urgency]} text-[10px]`}>{problem.urgency}</span>
        <span className="text-xs text-textMuted whitespace-nowrap">
          {problem.createdAt ? formatDistanceToNow(new Date(problem.createdAt), { addSuffix: true }) : 'Recently'}
        </span>
      </div>
    );
  }

  return (
    <div
      onClick={() => navigate(`/problems/${problem.id}`)}
      className={`bg-white border border-border rounded-xl p-5 hover:shadow-md transition-all cursor-pointer border-l-4 ${urgencyColors[problem.urgency]}`}
    >
      {/* Top Row: Category + Urgency + Status */}
      <div className="flex items-center gap-2 flex-wrap mb-2">
        <div className="flex items-center gap-1.5 text-sm text-textMuted">
          <span className="text-lg" dangerouslySetInnerHTML={{ __html: categoryIcons[problem.category] || '&#128203;' }} />
          <span className="text-xs font-medium">{problem.category?.replace('_', ' ')}</span>
        </div>
        <span className={`badge ${urgencyBadgeColors[problem.urgency]} text-[10px]`}>{problem.urgency}</span>
        <span className={`badge ${statusBadgeColors[problem.status]} text-[10px] ml-auto`}>
          {problem.status?.replace('_', ' ')}
        </span>
      </div>

      {/* Title */}
      <h3 className="text-base font-semibold text-primary line-clamp-2 mb-1.5">{problem.title}</h3>

      {/* Description */}
      <p className="text-sm text-textMuted line-clamp-3 mb-3">{problem.description}</p>

      {/* Author */}
      <div className="flex items-center gap-2 mb-3">
        <div className="w-6 h-6 rounded-full bg-primary flex items-center justify-center">
          <span className="text-white text-[10px] font-bold">{getInitials(problem.postedBy?.fullName)}</span>
        </div>
        <span className="text-xs text-textMain font-medium">{problem.postedBy?.fullName}</span>
        <span className="text-xs text-textMuted">{problem.postedBy?.flatNumber}</span>
        <span className="text-textMuted">·</span>
        <span className="text-xs text-textMuted">
          {problem.createdAt ? formatDistanceToNow(new Date(problem.createdAt), { addSuffix: true }) : 'Recently'}
        </span>
      </div>

      {/* Actions */}
      <div className="flex items-center gap-3 pt-3 border-t border-border">
        <button
          onClick={handleUpvote}
          className={`flex items-center gap-1.5 text-xs font-medium px-2.5 py-1.5 rounded-lg transition-colors ${
            upvoted ? 'bg-accent/10 text-accent' : 'bg-gray-50 text-textMuted hover:bg-gray-100'
          }`}
        >
          <ThumbsUp size={14} />
          <span>{upvoteCount || 0}</span>
        </button>

        <div className="flex items-center gap-1.5 text-xs text-textMuted">
          <MessageSquare size={14} />
          <span>{problem.commentCount || 0}</span>
        </div>

        <button
          onClick={handleVolunteer}
          className={`flex items-center gap-1.5 text-xs font-medium px-2.5 py-1.5 rounded-lg transition-colors ml-auto ${
            volunteer ? 'bg-accent/10 text-accent border border-accent/20' : 'border border-border text-textMuted hover:bg-gray-50'
          }`}
        >
          <HandHeart size={14} />
          <span>{volunteer ? 'Helping' : 'I can help'}</span>
        </button>

        <button className="text-textMuted hover:text-primary transition-colors">
          <ArrowRight size={16} />
        </button>
      </div>
    </div>
  );
}
