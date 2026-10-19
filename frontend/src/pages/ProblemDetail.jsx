import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  ArrowLeft, ThumbsUp, HandHeart, Trophy, Clock,
  CheckCircle, ChevronDown
} from 'lucide-react';
import { problemApi } from '../api/problemApi';
import { commentApi } from '../api/commentApi';
import { formatDistanceToNow } from 'date-fns';
import { useAuthStore } from '../store/authStore';

const urgencyColors = {
  CRITICAL: 'bg-danger text-white',
  HIGH: 'bg-warning text-white',
  MEDIUM: 'bg-accent text-primary',
  LOW: 'bg-success text-white',
};

const statusColors = {
  OPEN: 'bg-gray-100 text-gray-700',
  IN_PROGRESS: 'bg-blue-100 text-blue-700',
  RESOLVED: 'bg-success/10 text-success',
  ESCALATED: 'bg-danger/10 text-danger',
};

export default function ProblemDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user, isAdmin } = useAuthStore();
  const [problem, setProblem] = useState(null);
  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState('');
  const [isVolunteer, setIsVolunteer] = useState(false);
  const [activeTab, setActiveTab] = useState('comments');
  const [loading, setLoading] = useState(true);

  const fetchData = async () => {
    try {
      const [problemRes, commentsRes] = await Promise.all([
        problemApi.getById(id),
        commentApi.getByProblem(id),
      ]);
      setProblem(problemRes.data);
      setComments(commentsRes.data);
      setIsVolunteer(problemRes.data.userIsVolunteer);
    } catch (err) {
      console.error('Error fetching problem:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [id]);

  const handlePostComment = async () => {
    if (!newComment.trim()) return;
    try {
      await commentApi.create(id, { content: newComment, isVolunteerOffer });
      setNewComment('');
      setIsVolunteer(false);
      fetchData();
    } catch (err) {
      console.error('Post comment error:', err);
    }
  };

  const handleVolunteerToggle = async () => {
    try {
      await problemApi.toggleVolunteer(id);
      fetchData();
    } catch (err) {
      console.error(err);
    }
  };

  const handleUpvote = async () => {
    try {
      await problemApi.toggleUpvote(id);
      fetchData();
    } catch (err) {
      console.error(err);
    }
  };

  const getInitials = (name) => {
    if (!name) return '?';
    const parts = name.split(' ');
    if (parts.length >= 2) return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
    return name.substring(0, 2).toUpperCase();
  };

  if (loading) {
    return (
      <div className="space-y-4 animate-pulse">
        <div className="h-4 bg-gray-200 rounded w-1/4" />
        <div className="h-8 bg-gray-200 rounded w-3/4" />
        <div className="h-32 bg-gray-200 rounded" />
      </div>
    );
  }

  if (!problem) return <div className="text-center py-16">Problem not found</div>;

  return (
    <div className="space-y-6">
      {/* Breadcrumb */}
      <button onClick={() => navigate('/problems')} className="flex items-center gap-1 text-sm text-textMuted hover:text-textMain transition-colors">
        <ArrowLeft size={16} /> Back to Problem Board
      </button>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Left Column */}
        <div className="lg:col-span-2 space-y-6">
          {/* Problem Header */}
          <div className="card">
            <div className="flex items-center gap-2 flex-wrap mb-3">
              <span className={`badge ${urgencyColors[problem.urgency]} text-xs`}>{problem.urgency}</span>
              <span className={`badge ${statusColors[problem.status]} text-xs`}>{problem.status?.replace('_', ' ')}</span>
            </div>

            <h1 className="text-xl lg:text-2xl font-bold text-textMain mb-4">{problem.title}</h1>

            <div className="flex items-center gap-3 mb-4">
              <div className="w-8 h-8 rounded-full bg-primary flex items-center justify-center">
                <span className="text-white text-xs font-bold">{getInitials(problem.postedBy?.fullName)}</span>
              </div>
              <div>
                <span className="text-sm font-medium text-textMain">{problem.postedBy?.fullName}</span>
                <span className="text-textMuted mx-2">·</span>
                <span className="text-sm text-textMuted">{problem.postedBy?.flatNumber}</span>
                <span className="text-textMuted mx-2">·</span>
                <span className="text-sm text-textMuted">
                  {problem.createdAt ? formatDistanceToNow(new Date(problem.createdAt), { addSuffix: true }) : 'Recently'}
                </span>
              </div>
              <button onClick={handleUpvote}
                className={`ml-auto flex items-center gap-1.5 text-sm font-medium px-3 py-1.5 rounded-lg transition-colors ${
                  problem.userHasUpvoted ? 'bg-accent/10 text-accent' : 'bg-gray-50 text-textMuted hover:bg-gray-100'
                }`}>
                <ThumbsUp size={14} /> {problem.upvoteCount}
              </button>
            </div>

            <p className="text-base text-textMain leading-relaxed">{problem.description}</p>

            {problem.photoUrl && (
              <img src={problem.photoUrl} alt="Problem" className="mt-4 w-full max-h-[400px] object-cover rounded-xl" />
            )}
          </div>

          {/* Tabs */}
          <div className="card">
            <div className="flex gap-4 border-b border-border mb-4">
              {['comments', 'volunteers', 'timeline'].map(tab => (
                <button key={tab}
                  onClick={() => setActiveTab(tab)}
                  className={`pb-3 text-sm font-medium capitalize transition-colors ${
                    activeTab === tab ? 'text-primary border-b-2 border-primary' : 'text-textMuted hover:text-textMain'
                  }`}>
                  {tab === 'comments' ? 'Suggestions & Comments' : tab}
                </button>
              ))}
            </div>

            {activeTab === 'comments' && (
              <div className="space-y-4">
                {/* New Comment */}
                <div className="space-y-3">
                  <textarea
                    value={newComment}
                    onChange={(e) => setNewComment(e.target.value)}
                    placeholder="Write a suggestion or comment..."
                    rows={3}
                    className="w-full p-3 border border-border rounded-lg text-sm focus:border-accent focus:ring-[3px] focus:ring-accent/15 outline-none resize-none"
                  />
                  <div className="flex items-center justify-between">
                    <label className="flex items-center gap-2 text-sm text-textMuted cursor-pointer">
                      <input type="checkbox" checked={isVolunteer} onChange={(e) => setIsVolunteer(e.target.checked)}
                        className="w-4 h-4 rounded accent-accent" />
                      I am volunteering to fix this
                    </label>
                    <button onClick={handlePostComment} disabled={!newComment.trim()}
                      className="btn-primary text-sm px-4 h-9">Post</button>
                  </div>
                </div>

                {/* Comments List */}
                <div className="space-y-3 pt-4 border-t border-border">
                  {comments.map(comment => (
                    <div key={comment.id} className={`p-4 rounded-lg ${comment.isSolution ? 'bg-success/5 border border-success/20' : 'bg-gray-50'}`}>
                      {comment.isSolution && (
                        <div className="flex items-center gap-1 text-success text-xs font-medium mb-2">
                          <Trophy size={12} /> Accepted Solution
                        </div>
                      )}
                      <div className="flex items-start gap-3">
                        <div className="w-8 h-8 rounded-full bg-primary flex items-center justify-center flex-shrink-0">
                          <span className="text-white text-xs font-bold">{getInitials(comment.user?.fullName)}</span>
                        </div>
                        <div className="flex-1">
                          <div className="flex items-center gap-2 mb-1">
                            <span className="text-sm font-medium text-textMain">{comment.user?.fullName}</span>
                            <span className="text-xs text-textMuted">{comment.user?.flatNumber}</span>
                            <span className="text-textMuted">·</span>
                            <span className="text-xs text-textMuted">
                              {comment.createdAt ? formatDistanceToNow(new Date(comment.createdAt), { addSuffix: true }) : 'Recently'}
                            </span>
                          </div>
                          <p className="text-sm text-textMain">{comment.content}</p>
                          {comment.isVolunteerOffer && (
                            <span className="inline-flex items-center gap-1 mt-2 text-xs font-medium text-accent bg-accent/10 px-2 py-1 rounded">
                              <HandHeart size={12} /> Volunteering to help
                            </span>
                          )}
                        </div>
                      </div>
                    </div>
                  ))}
                  {comments.length === 0 && (
                    <p className="text-center text-textMuted text-sm py-8">No comments yet. Be the first to share your thoughts.</p>
                  )}
                </div>
              </div>
            )}

            {activeTab === 'volunteers' && (
              <div>
                {problem.volunteerCount > 0 ? (
                  <div className="space-y-3">
                    <p className="text-sm text-textMuted">{problem.volunteerCount} residents have volunteered to help</p>
                  </div>
                ) : (
                  <div className="text-center py-8">
                    <HandHeart size={40} className="text-textMuted mx-auto mb-3" />
                    <p className="text-textMuted text-sm">No volunteers yet. Be the first to volunteer!</p>
                  </div>
                )}
              </div>
            )}

            {activeTab === 'timeline' && (
              <div className="space-y-4">
                <div className="flex gap-3">
                  <div className="flex flex-col items-center">
                    <div className="w-3 h-3 rounded-full bg-primary" />
                    <div className="w-px h-full bg-border" />
                  </div>
                  <div className="pb-4">
                    <p className="text-sm font-medium text-textMain">Problem reported</p>
                    <p className="text-xs text-textMuted">
                      {problem.createdAt ? formatDistanceToNow(new Date(problem.createdAt), { addSuffix: true }) : 'Recently'}
                    </p>
                  </div>
                </div>
                {problem.status !== 'OPEN' && (
                  <div className="flex gap-3">
                    <div className="flex flex-col items-center">
                      <div className="w-3 h-3 rounded-full bg-blue-500" />
                      <div className="w-px h-full bg-border" />
                    </div>
                    <div className="pb-4">
                      <p className="text-sm font-medium text-textMain">Status changed to {problem.status?.replace('_', ' ')}</p>
                      <p className="text-xs text-textMuted">by {problem.assignedTo?.fullName || 'Admin'}</p>
                    </div>
                  </div>
                )}
                {problem.status === 'RESOLVED' && (
                  <div className="flex gap-3">
                    <div className="flex flex-col items-center">
                      <div className="w-3 h-3 rounded-full bg-success" />
                    </div>
                    <div>
                      <p className="text-sm font-medium text-success">Problem resolved</p>
                      <p className="text-xs text-textMuted">by {problem.resolvedBy?.fullName}</p>
                    </div>
                  </div>
                )}
              </div>
            )}
          </div>
        </div>

        {/* Right Column */}
        <div className="space-y-4">
          {/* Status Card */}
          <div className="card">
            <h3 className="text-sm font-medium text-textMuted mb-2">Current Status</h3>
            <div className={`inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-sm font-medium ${statusColors[problem.status]}`}>
              <CheckCircle size={14} />
              {problem.status?.replace('_', ' ')}
            </div>
            {isAdmin() && (
              <div className="mt-4 pt-4 border-t border-border">
                <label className="text-sm text-textMuted mb-2 block">Change Status</label>
                <select
                  onChange={(e) => problemApi.updateStatus(id, e.target.value).then(() => fetchData())}
                  className="input-field text-sm"
                  defaultValue=""
                >
                  <option value="" disabled>Update status...</option>
                  <option value="OPEN">Open</option>
                  <option value="IN_PROGRESS">In Progress</option>
                  <option value="RESOLVED">Resolved</option>
                  <option value="ESCALATED">Escalated</option>
                </select>
              </div>
            )}
          </div>

          {/* Actions Card */}
          <div className="card space-y-2">
            <button onClick={handleVolunteerToggle}
              className={`w-full h-10 rounded-lg text-sm font-medium flex items-center justify-center gap-2 transition-colors ${
                problem.userIsVolunteer
                  ? 'bg-accent/10 text-accent border border-accent/20'
                  : 'bg-gray-50 text-textMain border border-border hover:bg-gray-100'
              }`}>
              <HandHeart size={16} />
              {problem.userIsVolunteer ? 'You are volunteering' : 'I can help'}
            </button>
            <button onClick={() => navigate('/meetings')}
              className="w-full h-10 rounded-lg text-sm font-medium flex items-center justify-center gap-2 bg-primary text-white hover:bg-primary/90 transition-colors">
              <Clock size={16} /> Schedule a Meeting
            </button>
          </div>

          {/* Problem Info */}
          <div className="card space-y-3">
            <h3 className="text-sm font-medium text-textMuted">Details</h3>
            <div className="flex justify-between text-sm">
              <span className="text-textMuted">Category</span>
              <span className="font-medium">{problem.category?.replace('_', ' ')}</span>
            </div>
            <div className="flex justify-between text-sm">
              <span className="text-textMuted">Block</span>
              <span className="font-medium">{problem.block}</span>
            </div>
            <div className="flex justify-between text-sm">
              <span className="text-textMuted">Flat</span>
              <span className="font-medium">{problem.flatNumber}</span>
            </div>
            <div className="flex justify-between text-sm">
              <span className="text-textMuted">Urgency</span>
              <span className={`font-medium ${problem.urgency === 'CRITICAL' ? 'text-danger' : problem.urgency === 'HIGH' ? 'text-warning' : ''}`}>
                {problem.urgency}
              </span>
            </div>
            <div className="flex justify-between text-sm">
              <span className="text-textMuted">Upvotes</span>
              <span className="font-medium">{problem.upvoteCount}</span>
            </div>
            <div className="flex justify-between text-sm">
              <span className="text-textMuted">Comments</span>
              <span className="font-medium">{problem.commentCount}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
