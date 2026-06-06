import React from 'react';
import { api } from '../services/api';

export default function DynamicTable({ fields, records, onEdit, onDelete }) {
  const isAdmin = api.isAdmin();

  // Filter and sort fields to display
  const displayFields = [...fields]
    .filter(f => f.visible)
    .sort((a, b) => (a.display_order || 0) - (b.display_order || 0));

  const formatValue = (value, fieldType) => {
    if (value === null || value === undefined || value === '') {
      return <span style={{ color: 'var(--text-muted)' }}>—</span>;
    }

    if (fieldType === 'BOOLEAN') {
      return value ? (
        <span className="badge badge-success">Yes</span>
      ) : (
        <span className="badge badge-danger">No</span>
      );
    }

    if (fieldType === 'DATE') {
      try {
        // Date could be an array [yyyy, MM, dd] from Jackson
        if (Array.isArray(value)) {
          const [year, month, day] = value;
          const date = new Date(year, month - 1, day);
          return date.toLocaleDateString(undefined, { year: 'numeric', month: 'short', day: 'numeric' });
        }
        const date = new Date(value);
        return date.toLocaleDateString(undefined, { year: 'numeric', month: 'short', day: 'numeric' });
      } catch (e) {
        return String(value);
      }
    }

    return String(value);
  };

  return (
    <div className="table-container">
      <table className="custom-table">
        <thead>
          <tr>
            {displayFields.map((field) => (
              <th key={field.field_name}>{field.label}</th>
            ))}
            <th style={{ width: '100px', textAlign: 'center' }}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {records.map((record) => (
            <tr key={record.id}>
              {displayFields.map((field) => (
                <td key={field.field_name}>
                  {formatValue(record[field.field_name], field.field_type)}
                </td>
              ))}
              <td style={{ textAlign: 'center' }}>
                <div className="action-cell" style={{ justifyContent: 'center' }}>
                  <button
                    className="action-btn edit-btn"
                    onClick={() => onEdit(record)}
                    title="Edit Record"
                  >
                    ✏️
                  </button>
                  <button
                    className="action-btn delete-btn"
                    onClick={() => {
                      if (window.confirm('Are you sure you want to delete this record?')) {
                        onDelete(record.id);
                      }
                    }}
                    disabled={!isAdmin}
                    title={isAdmin ? 'Delete Record' : 'Requires Admin Authority'}
                  >
                    🗑️
                  </button>
                </div>
              </td>
            </tr>
          ))}
          {records.length === 0 && (
            <tr>
              <td colSpan={displayFields.length + 1} style={{ textAlign: 'center', padding: '3rem 1.5rem' }}>
                <div className="empty-state">
                  <span className="empty-state-icon">📂</span>
                  <span className="empty-state-title">No Records Found</span>
                  <span className="empty-state-desc">Create a new entry by clicking the "Add Record" button.</span>
                </div>
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}
