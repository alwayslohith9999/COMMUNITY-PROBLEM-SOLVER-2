import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Eye, EyeOff, CheckCircle, Info } from 'lucide-react';
import { useAuth } from '../hooks/useAuth';
import { useAuthStore } from '../store/authStore';

export default function Register() {
  const navigate = useNavigate();
  const { register } = useAuth();
  const { isLoading, error } = useAuthStore();
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const [agreed, setAgreed] = useState(false);
  const [formData, setFormData] = useState({
    fullName: '', email: '', password: '', confirmPassword: '',
    block: 'A', flatNumber: '', phoneNumber: '', moveInDate: ''
  });
  const [passwordMatch, setPasswordMatch] = useState(true);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (formData.password !== formData.confirmPassword) {
      setPasswordMatch(false);
      return;
    }
    if (!agreed) return;
    const { confirmPassword, ...registerData } = formData;
    const result = await register(registerData);
    if (result.success) {
      navigate('/');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center p-6 bg-surface">
      <div className="w-full max-w-lg">
        <div className="text-center mb-8">
          <div className="flex items-center justify-center gap-2 mb-4">
            <div className="w-10 h-10 bg-accent rounded-lg flex items-center justify-center">
              <span className="text-primary font-bold">CF</span>
            </div>
            <span className="text-primary font-heading font-semibold text-xl">CommunityFix</span>
          </div>
          <h1 className="text-[28px] font-heading font-bold text-textMain mb-2">Join your community</h1>
          <p className="text-textMuted">Register to start collaborating with your neighbors</p>
        </div>

        {error && (
          <div className="mb-4 p-3 bg-danger/10 border border-danger/20 rounded-lg text-danger text-sm">{error}</div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="input-label">Full Name <span className="text-danger">*</span></label>
            <input type="text" value={formData.fullName} onChange={(e) => setFormData({...formData, fullName: e.target.value})}
              placeholder="e.g. Rahul Sharma" className="input-field" required />
          </div>

          <div>
            <label className="input-label">Email <span className="text-danger">*</span></label>
            <input type="email" value={formData.email} onChange={(e) => setFormData({...formData, email: e.target.value})}
              placeholder="e.g. rahul@gmail.com" className="input-field" required />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="input-label">Password <span className="text-danger">*</span></label>
              <div className="relative">
                <input type={showPassword ? 'text' : 'password'} value={formData.password}
                  onChange={(e) => setFormData({...formData, password: e.target.value})}
                  placeholder="Min 6 characters" className="input-field pr-10" required />
                <button type="button" onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-textMuted">
                  {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
                </button>
              </div>
            </div>
            <div>
              <label className="input-label">Confirm Password <span className="text-danger">*</span></label>
              <div className="relative">
                <input type={showConfirm ? 'text' : 'password'} value={formData.confirmPassword}
                  onChange={(e) => { setFormData({...formData, confirmPassword: e.target.value}); setPasswordMatch(true); }}
                  placeholder="Re-enter password" className={`input-field pr-10 ${!passwordMatch ? 'error' : ''}`} required />
                <button type="button" onClick={() => setShowConfirm(!showConfirm)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-textMuted">
                  {showConfirm ? <EyeOff size={18} /> : <Eye size={18} />}
                </button>
              </div>
              {!passwordMatch && <p className="text-danger text-xs mt-1">Passwords do not match</p>}
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="input-label">Block <span className="text-danger">*</span></label>
              <select value={formData.block} onChange={(e) => setFormData({...formData, block: e.target.value})}
                className="input-field" required>
                <option value="A">Block A</option>
                <option value="B">Block B</option>
                <option value="C">Block C</option>
                <option value="D">Block D</option>
                <option value="E">Block E</option>
              </select>
            </div>
            <div>
              <label className="input-label">Flat Number <span className="text-danger">*</span></label>
              <input type="text" value={formData.flatNumber}
                onChange={(e) => setFormData({...formData, flatNumber: e.target.value.toUpperCase()})}
                placeholder="e.g. A-101" className="input-field" required
                pattern="^[A-E]-\d{3}$" title="Format: Block-Number e.g. A-101" />
              <p className="text-xs text-textMuted mt-1">Format: Block-Number e.g. A-101</p>
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="input-label">Phone Number <span className="text-danger">*</span></label>
              <div className="relative">
                <span className="absolute left-3 top-1/2 -translate-y-1/2 text-sm text-textMuted">+91</span>
                <input type="tel" value={formData.phoneNumber}
                  onChange={(e) => setFormData({...formData, phoneNumber: e.target.value.replace(/\D/g, '').slice(0, 10)})}
                  placeholder="9876543210" className="input-field pl-12" required pattern="\d{10}" />
              </div>
            </div>
            <div>
              <label className="input-label">Move-in Date <span className="text-danger">*</span></label>
              <input type="date" value={formData.moveInDate}
                onChange={(e) => setFormData({...formData, moveInDate: e.target.value})}
                className="input-field" required />
            </div>
          </div>

          <div className="flex items-start gap-2">
            <input type="checkbox" id="agree" checked={agreed} onChange={(e) => setAgreed(e.target.checked)}
              className="w-[18px] h-[18px] rounded accent-accent mt-0.5" />
            <label htmlFor="agree" className="text-sm text-textMuted">
              I agree to the <button type="button" className="text-primary hover:underline">community guidelines</button>
            </label>
          </div>

          <div className="p-4 bg-blue-50 border border-blue-100 rounded-lg flex gap-3">
            <Info size={18} className="text-blue-500 flex-shrink-0 mt-0.5" />
            <p className="text-xs text-blue-700">Your registration will be reviewed by the admin for approval. You will be notified once approved.</p>
          </div>

          <button type="submit" disabled={isLoading || !agreed} className="btn-accent w-full">
            {isLoading ? <div className="w-5 h-5 border-2 border-primary/30 border-t-primary rounded-full animate-spin" /> : 'Register'}
          </button>
        </form>

        <p className="mt-6 text-center text-sm text-textMuted">
          Already have an account? <Link to="/login" className="text-primary font-medium hover:underline">Sign in</Link>
        </p>
      </div>
    </div>
  );
}
