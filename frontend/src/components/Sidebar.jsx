import React from 'react';
import { api } from '../services/api';

export default function Sidebar({ forms, activeForm, onSelectForm, onLogout }) {
  const { username, roles } = api.getUserInfo();
  const isAdmin = api.isAdmin();

  return (
    <aside className="sidebar">
      <div className="sidebar-brand">
        <div className="brand-icon">F</div>
        <span className="brand-name">Form Engine</span>
      </div>

      <nav style={{ flexGrow: 1 }}>
        <h3 className="menu-label">Available Forms</h3>
        <ul className="sidebar-menu">
          {forms.map((form) => (
            <li key={form.id}>
              <div
                className={`menu-item ${activeForm === form.formName ? 'active' : ''}`}
                onClick={() => onSelectForm(form.formName)}
              >
                <span className="menu-item-icon">📄</span>
                <span>{form.description}</span>
              </div>
            </li>
          ))}
          {forms.length === 0 && (
            <div style={{ padding: '0.75rem', fontSize: '0.85rem', color: 'var(--text-muted)' }}>
              No forms loaded.
            </div>
          )}
        </ul>
      </nav>

      <div className="sidebar-footer">
        <div className="user-profile">
          <div className="avatar">
            {username ? username.charAt(0).toUpperCase() : '?'}
          </div>
          <div className="user-info">
            <span className="username">{username || 'Anonymous'}</span>
            <span className="user-role">
              {isAdmin ? (
                <span className="badge badge-primary" style={{ fontSize: '0.65rem', padding: '1px 6px' }}>Admin</span>
              ) : (
                <span className="badge badge-warning" style={{ fontSize: '0.65rem', padding: '1px 6px' }}>User</span>
              )}
            </span>
          </div>
        </div>

        <button onClick={onLogout} className="btn btn-danger" style={{ width: '100%' }}>
          Sign Out
        </button>
      </div>
    </aside>
  );
}
