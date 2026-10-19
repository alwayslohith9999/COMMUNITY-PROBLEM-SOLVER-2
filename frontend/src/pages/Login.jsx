import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Eye, EyeOff, CheckCircle, Building2 } from 'lucide-react';
import { useAuth } from '../hooks/useAuth';
import { useAuthStore } from '../store/authStore';

export default function Login() {
  const navigate = useNavigate();
  const { login } = useAuth();
  const { isLoading, error } = useAuthStore();
  const [showPassword, setShowPassword] = useState(false);
  const [formData, setFormData] = useState({ email: '', password: '' });

  const handleSubmit = async (e) => {
    e.preventDefault();
    const result = await login(formData.email, formData.password);
    if (result.success) {
      navigate('/');
    }
  };

  return (
    <div className="min-h-screen flex">
      {/* Left Panel */}
      <div className="hidden lg:flex lg:w-1/2 bg-primary flex-col justify-between p-10 relative overflow-hidden">
        <div className="absolute inset-0 opacity-10">
          <svg width="100%" height="100%" viewBox="0 0 800 800">
            <rect x="200" y="100" width="120" height="500" rx="4" fill="white" />
            <rect x="340" y="150" width="100" height="450" rx="4" fill="white" />
            <rect x="460" y="80" width="140" height="520" rx="4" fill="white" />
            <rect x="620" y="180" width="90" height="420" rx="4" fill="white" />
            <rect x="180" y="600" width="560" height="20" rx="4" fill="white" />
            <rect x="240" y="140" width="30" height="40" rx="2" fill="white" />
            <rect x="280" y="140" width="30" height="40" rx="2" fill="white" />
            <rect x="240" y="200" width="30" height="40" rx="2" fill="white" />
            <rect x="280" y="200" width="30" height="40" rx="2" fill="white" />
            <rect x="380" y="190" width="25" height="35" rx="2" fill="white" />
            <rect x="415" y="190" width="25" height="35" rx="2" fill="white" />
            <rect x="480" y="120" width="35" height="45" rx="2" fill="white" />
            <rect x="530" y="120" width="35" height="45" rx="2" fill="white" />
          </svg>
        </div>

        <div className="relative z-10">
          <div className="flex items-center gap-2">
            <div className="w-10 h-10 bg-accent rounded-lg flex items-center justify-center">
              <span className="text-primary font-bold">CF</span>
            </div>
            <span className="text-white font-heading font-semibold text-xl">CommunityFix</span>
          </div>
        </div>

        <div className="relative z-10 flex-1 flex flex-col justify-center">
          <Building2 size={120} className="text-white/20 mb-8" />
          <h2 className="text-white text-[28px] font-heading font-semibold mb-4">
            Your community, your voice.
          </h2>
          <div className="space-y-3">
            <div className="flex items-center gap-3 text-white/90">
              <CheckCircle size={20} className="text-accent flex-shrink-0" />
              <span className="text-sm">Report and track community problems</span>
            </div>
            <div className="flex items-center gap-3 text-white/90">
              <CheckCircle size={20} className="text-accent flex-shrink-0" />
              <span className="text-sm">Collaborate with neighbors on solutions</span>
            </div>
            <div className="flex items-center gap-3 text-white/90">
              <CheckCircle size={20} className="text-accent flex-shrink-0" />
              <span className="text-sm">Attend virtual community meetings</span>
            </div>
          </div>
        </div>

        <div className="relative z-10 text-white/50 text-xs">
          &copy; 2024 CommunityFix. All rights reserved.
        </div>
      </div>

      {/* Right Panel */}
      <div className="flex-1 flex flex-col justify-center items-center p-6 lg:p-12 bg-surface">
        <div className="w-full max-w-md">
          {/* Mobile Logo */}
          <div className="lg:hidden flex items-center justify-center gap-2 mb-8">
            <div className="w-10 h-10 bg-accent rounded-lg flex items-center justify-center">
              <span className="text-primary font-bold">CF</span>
            </div>
            <span className="text-primary font-heading font-semibold text-xl">CommunityFix</span>
          </div>

          <div className="mb-8">
            <h1 className="text-[28px] font-heading font-bold text-textMain mb-2">Welcome back</h1>
            <p className="text-textMuted">Sign in to your community account</p>
          </div>

          {error && (
            <div className="mb-4 p-3 bg-danger/10 border border-danger/20 rounded-lg text-danger text-sm">
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-5">
            <div>
              <label className="input-label">Email</label>
              <input
                type="email"
                value={formData.email}
                onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                placeholder="e.g. your@email.com"
                className="input-field"
                required
              />
            </div>

            <div>
              <label className="input-label">Password</label>
              <div className="relative">
                <input
                  type={showPassword ? 'text' : 'password'}
                  value={formData.password}
                  onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                  placeholder="Enter your password"
                  className="input-field pr-10"
                  required
                />
                <button
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-textMuted hover:text-textMain"
                >
                  {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                </button>
              </div>
            </div>

            <div className="flex justify-end">
              <button type="button" className="text-sm text-primary hover:underline">
                Forgot password?
              </button>
            </div>

            <button
              type="submit"
              disabled={isLoading}
              className="btn-primary w-full"
            >
              {isLoading ? (
                <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
              ) : (
                'Sign In'
              )}
            </button>
          </form>

          <div className="mt-6 flex items-center gap-4">
            <div className="flex-1 h-px bg-border" />
            <span className="text-sm text-textMuted">or</span>
            <div className="flex-1 h-px bg-border" />
          </div>

          <p className="mt-6 text-center text-sm text-textMuted">
            New resident?{' '}
            <Link to="/register" className="text-primary font-medium hover:underline">
              Register here
            </Link>
          </p>

          <div className="mt-8 p-4 bg-blue-50 border border-blue-100 rounded-lg">
            <p className="text-xs text-blue-700 font-medium mb-1">Demo Credentials</p>
            <p className="text-xs text-blue-600">Admin: admin@communityfix.com / password123</p>
            <p className="text-xs text-blue-600">Resident: arjun@gmail.com / password123</p>
          </div>
        </div>
      </div>
    </div>
  );
}
