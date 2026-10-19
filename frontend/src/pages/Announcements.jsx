import { useEffect, useState } from 'react';
import { Megaphone, Pin, ThumbsUp, HelpCircle, CheckCircle2 } from 'lucide-react';
import { announcementApi } from '../api/announcementApi';
import { formatDistanceToNow } from 'date-fns';

export default function Announcements() {
  const [announcements, setAnnouncements] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    announcementApi.getAll().then(res => {
      setAnnouncements(res.data);
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

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-heading font-bold">Announcements</h1>

      {loading ? (
        <div className="space-y-4">
          {[1,2,3].map(i => <div key={i} className="card h-32 animate-pulse" />)}
        </div>
      ) : (
        <div className="space-y-4">
          {announcements.map(a => (
            <div key={a.id} className={`card ${a.pinned ? 'border-l-4 border-l-accent' : ''}`}>
              <div className="flex items-start gap-4">
                <div className="w-10 h-10 rounded-full bg-primary flex items-center justify-center flex-shrink-0">
                  <span className="text-white text-xs font-bold">{getInitials(a.postedBy?.fullName)}</span>
                </div>
                <div className="flex-1">
                  <div className="flex items-center gap-2 mb-1">
                    {a.pinned && <Pin size={14} className="text-accent" />}
                    <h3 className="text-lg font-semibold text-textMain">{a.title}</h3>
                  </div>
                  <p className="text-sm text-textMain leading-relaxed mb-3">{a.content}</p>
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-3 text-xs text-textMuted">
                      <span>By {a.postedBy?.fullName}</span>
                      <span>·</span>
                      <span>{a.createdAt ? formatDistanceToNow(new Date(a.createdAt), { addSuffix: true }) : 'Recently'}</span>
                    </div>
                    <div className="flex items-center gap-3">
                      <span className="flex items-center gap-1 text-xs text-textMuted">
                        <ThumbsUp size={12} /> {a.thumbsUpCount || 0}
                      </span>
                      {a.questionCount > 0 && (
                        <span className="flex items-center gap-1 text-xs text-textMuted">
                          <HelpCircle size={12} /> {a.questionCount}
                        </span>
                      )}
                      {a.checkCount > 0 && (
                        <span className="flex items-center gap-1 text-xs text-textMuted">
                          <CheckCircle2 size={12} /> {a.checkCount}
                        </span>
                      )}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      {announcements.length === 0 && !loading && (
        <div className="text-center py-16">
          <Megaphone size={48} className="text-textMuted mx-auto mb-4" />
          <p className="text-textMuted">No announcements yet.</p>
        </div>
      )}
    </div>
  );
}
