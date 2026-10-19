import { useParams, useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { PhoneOff, Users, MessageSquare } from 'lucide-react';
import { meetingApi } from '../api/meetingApi';

export default function MeetingRoom() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [meeting, setMeeting] = useState(null);

  useEffect(() => {
    meetingApi.getById(id).then(res => {
      setMeeting(res.data);
    }).catch(err => {
      console.error(err);
      navigate('/meetings');
    });
  }, [id, navigate]);

  if (!meeting) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="w-8 h-8 border-2 border-primary/30 border-t-primary rounded-full animate-spin" />
      </div>
    );
  }

  const jitsiUrl = `https://meet.jit.si/${meeting.jitsiRoom}#config.prejoinPageEnabled=false&config.startWithAudioMuted=true`;

  return (
    <div className="fixed inset-0 bg-dark-surface z-50 flex flex-col">
      {/* Top Bar */}
      <div className="h-14 bg-dark-card border-b border-dark-border flex items-center justify-between px-4 flex-shrink-0">
        <div>
          <h2 className="text-white font-medium text-sm">{meeting.title}</h2>
          {meeting.problem && (
            <p className="text-dark-textMuted text-xs">About: {meeting.problem.title}</p>
          )}
        </div>
        <div className="flex items-center gap-4">
          <div className="flex items-center gap-1.5 text-dark-textMuted text-xs">
            <Users size={14} />
            <span>{meeting.attendeeCount || 0}</span>
          </div>
          <button
            onClick={() => navigate('/meetings')}
            className="flex items-center gap-1.5 bg-danger text-white text-xs font-medium px-3 py-1.5 rounded-lg hover:bg-danger/90 transition-colors"
          >
            <PhoneOff size={14} /> Leave
          </button>
        </div>
      </div>

      {/* Jitsi iframe */}
      <div className="flex-1">
        <iframe
          src={jitsiUrl}
          allow="camera; microphone; fullscreen; display-capture"
          className="w-full h-full border-0"
        />
      </div>
    </div>
  );
}
