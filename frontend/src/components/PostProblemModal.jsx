import { useState } from 'react';
import { X, Upload, AlertTriangle, AlertOctagon, ArrowDown, ArrowUp } from 'lucide-react';
import { problemApi } from '../api/problemApi';
import { useAuthStore } from '../store/authStore';

const categories = [
  'PLUMBING', 'ELECTRICAL', 'STRUCTURAL', 'NOISE', 'SECURITY',
  'PARKING', 'LIFT', 'COMMON_AREA', 'OTHER'
];

const urgencyLevels = [
  { value: 'LOW', label: 'Low', color: 'border-success', bgColor: 'bg-success/5', icon: ArrowDown },
  { value: 'MEDIUM', label: 'Medium', color: 'border-accent', bgColor: 'bg-accent/5', icon: ArrowUp },
  { value: 'HIGH', label: 'High', color: 'border-warning', bgColor: 'bg-warning/5', icon: AlertTriangle },
  { value: 'CRITICAL', label: 'Critical', color: 'border-danger', bgColor: 'bg-danger/5', icon: AlertOctagon },
];

export default function PostProblemModal({ onClose }) {
  const { user } = useAuthStore();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [formData, setFormData] = useState({
    title: '', category: '', urgency: '', description: '',
    block: user?.block || 'A', flatNumber: user?.flatNumber || '',
    notifyNeighbors: false
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    try {
      await problemApi.create(formData);
      onClose();
    } catch (err) {
      console.error('Create problem error:', err);
      alert(err.response?.data?.message || 'Failed to create problem');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div className="absolute inset-0 bg-black/50 backdrop-blur-sm" onClick={onClose} />
      <div className="relative bg-white rounded-2xl shadow-xl w-full max-w-[560px] max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-border">
          <h2 className="text-xl font-heading font-semibold">Report a Problem</h2>
          <button onClick={onClose} className="p-1.5 rounded-lg hover:bg-gray-100 transition-colors">
            <X size={20} />
          </button>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="p-6 space-y-5">
          {/* Title */}
          <div>
            <label className="input-label">Problem Title <span className="text-danger">*</span></label>
            <input
              type="text"
              value={formData.title}
              onChange={(e) => setFormData({ ...formData, title: e.target.value })}
              placeholder="e.g. Water leakage in corridor"
              className="input-field"
              required
              maxLength={200}
            />
          </div>

          {/* Category */}
          <div>
            <label className="input-label">Category <span className="text-danger">*</span></label>
            <div className="grid grid-cols-3 gap-2">
              {categories.map(cat => (
                <button
                  key={cat}
                  type="button"
                  onClick={() => setFormData({ ...formData, category: cat })}
                  className={`h-10 rounded-lg text-xs font-medium border transition-all ${
                    formData.category === cat
                      ? 'border-accent bg-accent/10 text-accent'
                      : 'border-border text-textMuted hover:border-textMuted'
                  }`}
                >
                  {cat.replace('_', ' ')}
                </button>
              ))}
            </div>
          </div>

          {/* Urgency */}
          <div>
            <label className="input-label">Urgency Level <span className="text-danger">*</span></label>
            <div className="grid grid-cols-4 gap-2">
              {urgencyLevels.map(u => (
                <button
                  key={u.value}
                  type="button"
                  onClick={() => setFormData({ ...formData, urgency: u.value })}
                  className={`flex flex-col items-center gap-1.5 p-3 rounded-lg border-2 transition-all ${
                    formData.urgency === u.value
                      ? `${u.color} ${u.bgColor}`
                      : 'border-border hover:border-textMuted'
                  }`}
                >
                  <u.icon size={18} className={formData.urgency === u.value ? '' : 'text-textMuted'} />
                  <span className="text-xs font-medium">{u.label}</span>
                </button>
              ))}
            </div>
          </div>

          {/* Description */}
          <div>
            <label className="input-label">Description <span className="text-danger">*</span></label>
            <textarea
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              placeholder="Describe the problem in detail..."
              rows={4}
              className="w-full p-3 border border-border rounded-lg text-sm focus:border-accent focus:ring-[3px] focus:ring-accent/15 outline-none resize-none"
              required
              maxLength={1000}
            />
            <div className="flex justify-end mt-1">
              <span className="text-xs text-textMuted">{formData.description.length}/1000</span>
            </div>
          </div>

          {/* Block & Flat */}
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="input-label">Block <span className="text-danger">*</span></label>
              <select
                value={formData.block}
                onChange={(e) => setFormData({ ...formData, block: e.target.value })}
                className="input-field"
                required
              >
                {['A', 'B', 'C', 'D', 'E'].map(b => <option key={b} value={b}>Block {b}</option>)}
              </select>
            </div>
            <div>
              <label className="input-label">Flat Number <span className="text-danger">*</span></label>
              <input
                type="text"
                value={formData.flatNumber}
                onChange={(e) => setFormData({ ...formData, flatNumber: e.target.value.toUpperCase() })}
                placeholder="e.g. A-101"
                className="input-field"
                required
                pattern="^[A-E]-\d{3}$"
              />
            </div>
          </div>

          {/* Notify Toggle */}
          <div className="flex items-center gap-3">
            <button
              type="button"
              onClick={() => setFormData({ ...formData, notifyNeighbors: !formData.notifyNeighbors })}
              className={`relative w-10 h-[22px] rounded-full transition-colors ${
                formData.notifyNeighbors ? 'bg-accent' : 'bg-gray-300'
              }`}
            >
              <div className={`absolute top-0.5 w-[18px] h-[18px] rounded-full bg-white shadow-sm transition-transform ${
                formData.notifyNeighbors ? 'translate-x-[18px]' : 'translate-x-0.5'
              }`} />
            </button>
            <span className="text-sm text-textMain">Alert residents in your block</span>
          </div>

          {/* Actions */}
          <div className="flex gap-3 pt-2">
            <button type="button" onClick={onClose} className="btn-outline flex-1">Cancel</button>
            <button type="submit" disabled={isSubmitting} className="btn-primary flex-1">
              {isSubmitting ? (
                <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
              ) : (
                'Submit Problem'
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
