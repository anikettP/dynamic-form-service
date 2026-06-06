import React, { useState } from 'react';
import { api } from '../services/api';

export default function LoginForm({ onLoginSuccess, addToast }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!username.trim() || !password.trim()) {
      setError('Username and password are required');
      return;
    }

    setLoading(true);
    setError('');
    
    try {
      await api.login(username.trim(), password);
      addToast('Logged in successfully', 'success');
      onLoginSuccess();
    } catch (err) {
      setError(err.message || 'Authentication failed. Please verify credentials.');
      addToast(err.message || 'Login failed', 'error');
    } finally {
      setLoading(false);
    }
  };

  const handlePrefill = (user, pass) => {
    setUsername(user);
    setPassword(pass);
    setError('');
  };

  return (
    <div className="auth-page">
      <div className="auth-card">
        <div className="auth-header">
          <div className="auth-logo">✦</div>
          <h1 className="auth-title">Form Service</h1>
          <p className="auth-desc">Metadata-Driven Dynamic Form Engine</p>
        </div>

        <form onSubmit={handleSubmit} className="form-grid">
          {error && (
            <div className="badge badge-danger grid-span-2" style={{ padding: '0.75rem', borderRadius: '8px', textAlign: 'center' }}>
              {error}
            </div>
          )}

          <div className="form-group grid-span-2">
            <label className="form-label" htmlFor="username">Username</label>
            <input
              id="username"
              type="text"
              className="form-control"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Enter username"
              disabled={loading}
            />
          </div>

          <div className="form-group grid-span-2">
            <label className="form-label" htmlFor="password">Password</label>
            <input
              id="password"
              type="password"
              className="form-control"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="••••••••"
              disabled={loading}
            />
          </div>

          <button
            type="submit"
            className="btn btn-primary grid-span-2"
            disabled={loading}
            style={{ marginTop: '1rem', height: '45px' }}
          >
            {loading ? 'Authenticating...' : 'Sign In'}
          </button>
        </form>

        <div className="prefill-section">
          <span className="prefill-title">Testing Profiles</span>
          <div className="prefill-buttons">
            <button
              type="button"
              className="prefill-btn"
              onClick={() => handlePrefill('admin', 'admin123')}
              disabled={loading}
            >
              Admin Role
            </button>
            <button
              type="button"
              className="prefill-btn"
              onClick={() => handlePrefill('user', 'admin123')}
              disabled={loading}
            >
              Standard User
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
