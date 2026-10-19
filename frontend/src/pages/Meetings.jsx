import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  CalendarDays, Plus, Radio, Users, ArrowRight, Video
} from 'lucide-react';
import { meetingApi } from '../api/meetingApi';
import { formatDistanceToNow, format } from 'date-fns';

export default function Meetings() {
  const navigate = useNavigate();
  const [meetings, setMeetings] = useState([]);
  const [liveMeetings, setLiveMeetings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showScheduleModal, setShowScheduleModal] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [allRes, liveRes] = await Promise.all([
          meetingApi.getAll(),
          meetingApi.getLive(),
        ]);
        setMeetings(allRes.data);
        setLiveMeetings(liveRes.data);
      } catch (err) {
        console.error('Fetch meetings error:', err);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const getInitials = (name) => {
    if (!name) return '?';
    const parts = name.split(' ');
    if (parts.length >= 2) return (parts[0][0] + parts[parts.length - 1][0]).toUpperCase();
    return name.substring(0, 2).toUpperCase();
  };

  if (loading) {
    return (
      <div className="space-y-4">
        <div className="flex justify-between">
          <div className="h-8 bg-gray-200 rounded w-1/4 animate-pulse" />
          <div className="h-10 bg-gray-200 rounded w-32 animate-pulse" />
        </div>
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
          {[1,2,3,4].map(i => <div key={i} className="card h-40 animate-pulse" />)}
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-heading font-bold">Community Meetings</h1>
        <button onClick={() => setShowScheduleModal(true)} className="btn-primary">
          <Plus size={18} /> Schedule Meeting
        </button>
      </div>

      {/* Live Now */}
      {liveMeetings.length > 0 && (
        <div className="space-y-3">
          <h2 className="text-sm font-medium text-textMuted uppercase tracking-wider flex items-center gap-2">
            <Radio size={14} className="text-danger" /> Live Now
          </h2>
          {liveMeetings.map(meeting => (
            <div key={meeting.id} className="card bg-success/5 border-success/20">
              <div className="flex items-center justify-between">
                <div>
                  <div className="flex items-center gap-2 mb-1">
                    <span className="w-2 h-2 rounded-full bg-success animate-pulse" />
                    <span className="text-xs font-medium text-success">Happening Now</span>
                  </div>
                  <h3 className="text-lg font-semibold text-textMain">{meeting.title}</h3>
                </div>
                <button
                  onClick={() => navigate(`/meetings/${meeting.id}`)}
                  className="btn-primary bg-success hover:bg-success/90"
                >
                  <Video size={16} /> Join Now
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Upcoming Meetings */}
      <div className="space-y-4">
        <h2 className="text-sm font-medium text-textMuted uppercase tracking-wider">
          Upcoming Meetings
        </h2>
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
          {meetings.map(meeting => (
            <div key={meeting.id} className="card hover:shadow-lg transition-all">
              {meeting.problem && (
                <div className="flex items-center gap-2 mb-3">
                  <span className="badge bg-primary/10 text-primary text-[10px]">
                    {meeting.problem.category?.replace('_', ' ')}
                  </span>
                </div>
              )}

              <h3 className="text-base font-semibold text-textMain mb-1">{meeting.title}</h3>

              {meeting.problem && (
                <p className="text-xs text-textMuted mb-3">About: {meeting.problem.title}</p>
              )}

              <div className="flex items-center gap-2 mb-3">
                <div className="w-6 h-6 rounded-full bg-primary flex items-center justify-center">
                  <span className="text-white text-[10px] font-bold">{getInitials(meeting.host?.fullName)}</span>
                </div>
                <span className="text-sm text-textMain">{meeting.host?.fullName}</span>
              </div>

              <div className="flex items-center gap-2 text-sm text-textMuted mb-4">
                <CalendarDays size={14} />
                <span>
                  {meeting.scheduledAt
                    ? format(new Date(meeting.scheduledAt), 'EEEE, d MMM · h:mm a')
                    : 'TBD'}
                </span>
              </div>

              <div className="flex items-center justify-between pt-3 border-t border-border">
                <div className="flex items-center">
                  <div className="flex -space-x-2">
                    {(meeting.attendees || []).slice(0, 4).map((att, i) => (
                      <div key={i} className="w-7 h-7 rounded-full bg-primary border-2 border-white flex items-center justify-center">
                        <span className="text-white text-[9px] font-bold">{getInitials(att?.fullName)}</span>
                      </div>
                    ))}
                  </div>
                  {(meeting.attendeeCount || 0) > 4 && (
                    <span className="ml-2 text-xs text-textMuted">+{meeting.attendeeCount - 4} others</span>
                  )}
                </div>
                <button
                  onClick={() => navigate(`/meetings/${meeting.id}`)}
                  className="flex items-center gap-1 text-sm text-primary font-medium hover:underline"
                >
                  Join <ArrowRight size={14} />
                </button>
              </div>
            </div>
          ))}
        </div>

        {meetings.length === 0 && (
          <div className="text-center py-16">
            <CalendarDays size={48} className="text-textMuted mx-auto mb-4" />
            <h3 className="text-lg font-medium text-textMain mb-2">No meetings scheduled</h3>
            <p className="text-textMuted text-sm mb-4">Be the first to schedule a community meeting.</p>
            <button onClick={() => setShowScheduleModal(true)} className="btn-primary">
              <Plus size={16} /> Schedule Meeting
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
