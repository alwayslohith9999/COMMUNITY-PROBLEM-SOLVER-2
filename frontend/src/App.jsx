import { Routes, Route, Navigate } from 'react-router-dom';
import { useEffect } from 'react';
import { useAuthStore } from './store/authStore';
import Layout from './components/Layout';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import ProblemBoard from './pages/ProblemBoard';
import ProblemDetail from './pages/ProblemDetail';
import Meetings from './pages/Meetings';
import MeetingRoom from './pages/MeetingRoom';
import Directory from './pages/Directory';
import Announcements from './pages/Announcements';
import Leaderboard from './pages/Leaderboard';
import Profile from './pages/Profile';
import Admin from './pages/Admin';

function App() {
  const { isAuthenticated } = useAuthStore();

  useEffect(() => {
    // Check for stored auth on mount
    const token = localStorage.getItem('accessToken');
    if (token && !isAuthenticated) {
      // Token exists but state not set - keep it for axios interceptor
    }
  }, [isAuthenticated]);

  return (
    <Routes>
      {/* Public Routes */}
      <Route path="/login" element={isAuthenticated ? <Navigate to="/" /> : <Login />} />
      <Route path="/register" element={isAuthenticated ? <Navigate to="/" /> : <Register />} />

      {/* Protected Routes */}
      <Route element={<Layout />}>
        <Route path="/" element={<Dashboard />} />
        <Route path="/problems" element={<ProblemBoard />} />
        <Route path="/problems/:id" element={<ProblemDetail />} />
        <Route path="/meetings" element={<Meetings />} />
        <Route path="/meetings/:id" element={<MeetingRoom />} />
        <Route path="/directory" element={<Directory />} />
        <Route path="/announcements" element={<Announcements />} />
        <Route path="/leaderboard" element={<Leaderboard />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/settings" element={<Profile />} />
        <Route path="/admin" element={<Admin />} />
      </Route>

      {/* Fallback */}
      <Route path="*" element={<Navigate to="/" />} />
    </Routes>
  );
}

export default App;
