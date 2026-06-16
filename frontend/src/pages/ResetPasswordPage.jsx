import { useState, useEffect } from 'react';
import { useSearchParams, useNavigate, Link } from 'react-router-dom';
import authService from '../services/authService';
import '../styles/auth.css';

export default function ResetPasswordPage() {
  const [searchParams] = useSearchParams();
  const token = searchParams.get('token');
  const navigate = useNavigate();

  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);
  const [validating, setValidating] = useState(true);

  useEffect(() => {
    const validateToken = async () => {
      if (!token) {
        setError('Invalid reset link');
        setValidating(false);
        return;
      }

      try {
        await authService.validateToken(token);
        setValidating(false);
      } catch (err) {
        setError('Invalid or expired reset link');
        setValidating(false);
      }
    };

    validateToken();
  }, [token]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setLoading(true);

    try {
      await authService.resetPassword({
        token,
        newPassword,
        confirmPassword,
      });
      setSuccess('Password reset successfully. Redirecting to login...');
      setTimeout(() => navigate('/login'), 2000);
    } catch (err) {
      setError(err.response?.data?.message || 'Reset failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  if (validating) {
    return (
      <div className="auth-container">
        <div className="auth-card">
          <p>Validating reset link...</p>
        </div>
      </div>
    );
  }

  if (error && validating === false) {
    return (
      <div className="auth-container">
        <div className="auth-card">
          <h2>Invalid Link</h2>
          <p className="error-message">{error}</p>
          <Link to="/forgot-password" className="submit-btn" style={{ textAlign: 'center', display: 'block' }}>
            Request New Link
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h1>Campus Connect</h1>
        <h2>Reset Password</h2>
        
        {error && <div className="error-message">{error}</div>}
        {success && <div className="success-message">{success}</div>}
        
        <form onSubmit={handleSubmit} className="auth-form">
          <div className="form-group">
            <label htmlFor="newPassword">New Password</label>
            <input
              type="password"
              id="newPassword"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              required
              placeholder="Enter new password"
            />
          </div>

          <div className="form-group">
            <label htmlFor="confirmPassword">Confirm Password</label>
            <input
              type="password"
              id="confirmPassword"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
              placeholder="Confirm new password"
            />
          </div>

          <button type="submit" disabled={loading} className="submit-btn">
            {loading ? 'Resetting...' : 'Reset Password'}
          </button>
        </form>

        <div className="auth-links">
          <Link to="/login">Back to Login</Link>
        </div>
      </div>
    </div>
  );
}
