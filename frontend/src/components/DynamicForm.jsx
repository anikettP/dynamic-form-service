import React, { useState, useEffect } from 'react';

// Date utility to convert backend values into native YYYY-MM-DD inputs
const formatDateForInput = (val) => {
  if (!val) return '';
  if (Array.isArray(val)) {
    const [year, month, day] = val;
    return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
  }
  if (typeof val === 'string') {
    return val.substring(0, 10);
  }
  return '';
};

export default function DynamicForm({ fields, initialData, onSubmit, onCancel, apiErrors }) {
  const [formData, setFormData] = useState({});
  const [errors, setErrors] = useState({});

  // Populate form data on initial load / edit mode
  useEffect(() => {
    const data = {};
    fields.forEach((field) => {
      const fieldName = field.field_name;
      let rawVal = initialData ? initialData[fieldName] : undefined;

      if (field.field_type === 'BOOLEAN') {
        data[fieldName] = rawVal !== undefined ? !!rawVal : false;
      } else if (field.field_type === 'DATE') {
        data[fieldName] = formatDateForInput(rawVal);
      } else {
        data[fieldName] = rawVal !== undefined ? rawVal : '';
      }
    });
    setFormData(data);
    setErrors({});
  }, [fields, initialData]);

  // Sync API errors returned by spring boot (e.g. ValidationException maps)
  useEffect(() => {
    if (apiErrors) {
      const mappedErrors = {};
      Object.keys(apiErrors).forEach((key) => {
        const snakeKey = key.replace(/[A-Z]/g, (letter) => `_${letter.toLowerCase()}`);
        mappedErrors[snakeKey] = apiErrors[key];
      });
      setErrors(mappedErrors);
    }
  }, [apiErrors]);

  // Evaluates if a field's dependency conditions are met
  const isDependencyMet = (field) => {
    if (!field.depends_on_field) return true;
    const parentVal = formData[field.depends_on_field];
    const expectedVal = field.depends_on_value;
    
    if (parentVal === undefined || parentVal === null) return false;
    return String(parentVal).toLowerCase() === String(expectedVal).toLowerCase();
  };

  const handleInputChange = (fieldName, val, fieldType) => {
    setFormData((prev) => {
      const updated = { ...prev, [fieldName]: val };
      
      // If we change a field that others depend on, clear dependent fields if condition is broken
      fields.forEach((f) => {
        if (f.depends_on_field === fieldName) {
          const parentVal = val;
          const expectedVal = f.depends_on_value;
          const met = String(parentVal).toLowerCase() === String(expectedVal).toLowerCase();
          if (!met) {
            updated[f.field_name] = f.field_type === 'BOOLEAN' ? false : '';
          }
        }
      });
      
      return updated;
    });

    // Clear error for this field
    if (errors[fieldName]) {
      setErrors((prev) => {
        const copy = { ...prev };
        delete copy[fieldName];
        return copy;
      });
    }
  };

  // Perform client-side validation
  const validateForm = () => {
    const newErrors = {};

    fields.forEach((field) => {
      const name = field.field_name;
      const label = field.label;
      const val = formData[name];
      const isVisible = field.visible && isDependencyMet(field);

      if (!isVisible) return;

      // Required validation
      let required = field.required;
      // disability_percentage is required if handicapped is true
      if (name === 'disability_percentage' && formData['handicapped'] === true) {
        required = true;
      }

      const isMissing = val === undefined || val === null || (typeof val === 'string' && val.trim() === '');
      if (required && isMissing) {
        newErrors[name] = `${label} is required`;
        return;
      }

      if (isMissing) return; // Skip other validations for empty non-required fields

      // Type validations
      if (field.field_type === 'NUMBER') {
        if (isNaN(Number(val))) {
          newErrors[name] = `${label} must be a valid number`;
          return;
        }
      }

      // Length validation
      if (field.min_length !== null && field.min_length !== undefined) {
        if (String(val).length < field.min_length) {
          newErrors[name] = `${label} must be at least ${field.min_length} characters`;
          return;
        }
      }
      if (field.max_length !== null && field.max_length !== undefined) {
        if (String(val).length > field.max_length) {
          newErrors[name] = `${label} must not exceed ${field.max_length} characters`;
          return;
        }
      }

      // Regex validation
      if (field.regex_pattern) {
        const regex = new RegExp(field.regex_pattern);
        if (!regex.test(String(val))) {
          // Provide customized messaging for common fields
          if (field.regex_pattern === '^\\d{12}$') {
            newErrors[name] = `${label} must be exactly 12 digits`;
          } else if (field.regex_pattern === '^[A-Z]{5}[0-9]{4}[A-Z]{1}$') {
            newErrors[name] = `Invalid ${label} format (e.g. ABCDE1234F)`;
          } else if (field.regex_pattern === '^[6-9]\\d{9}$') {
            newErrors[name] = `Invalid ${label} (must be a valid 10-digit number starting with 6-9)`;
          } else {
            newErrors[name] = `${label} format is invalid`;
          }
        }
      }
    });

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validateForm()) {
      // Map form fields and parse numbers
      const payload = {};
      fields.forEach((field) => {
        const name = field.field_name;
        if (field.visible && isDependencyMet(field)) {
          const val = formData[name];
          if (field.field_type === 'NUMBER' && val !== '') {
            payload[name] = Number(val);
          } else {
            payload[name] = val;
          }
        } else {
          // Set null/default for hidden dependent fields
          payload[name] = field.field_type === 'BOOLEAN' ? false : null;
        }
      });
      onSubmit(payload);
    }
  };

  // Sort fields based on display_order
  const sortedFields = [...fields].sort((a, b) => (a.display_order || 0) - (b.display_order || 0));

  return (
    <form onSubmit={handleSubmit} className="form-grid">
      {sortedFields.map((field) => {
        const name = field.field_name;
        const label = field.label;
        const type = field.field_type;
        const required = field.required || (name === 'disability_percentage' && formData['handicapped'] === true);
        
        // Hide if fields dependencies aren't met
        if (!field.visible || !isDependencyMet(field)) return null;

        const error = errors[name];
        const isEditable = field.editable && (!initialData || name !== 'id');

        // Check if BOOLEAN
        if (type === 'BOOLEAN') {
          return (
            <div key={name} className="form-group grid-span-2">
              <label className="toggle-container">
                <input
                  type="checkbox"
                  className="toggle-input"
                  checked={!!formData[name]}
                  onChange={(e) => handleInputChange(name, e.target.checked, type)}
                  disabled={!isEditable}
                />
                <div className="toggle-slider"></div>
                <span className="form-label">{label}</span>
              </label>
              {error && <span className="input-error">⚠ {error}</span>}
            </div>
          );
        }

        // Check if DATE
        const isDateType = type === 'DATE';
        // Check if NUMBER
        const isNumberType = type === 'NUMBER';

        return (
          <div key={name} className={`form-group ${name === 'remarks' ? 'grid-span-2' : ''}`}>
            <label className={`form-label ${required ? 'required' : ''}`} htmlFor={`input-${name}`}>
              {label}
            </label>
            
            {name === 'remarks' ? (
              <textarea
                id={`input-${name}`}
                className="form-control"
                style={{ minHeight: '80px', resize: 'vertical' }}
                value={formData[name] || ''}
                onChange={(e) => handleInputChange(name, e.target.value, type)}
                disabled={!isEditable}
                placeholder={`Enter ${label.toLowerCase()}`}
              />
            ) : (
              <input
                id={`input-${name}`}
                type={isDateType ? 'date' : isNumberType ? 'number' : 'text'}
                step={isNumberType ? 'any' : undefined}
                className="form-control"
                value={formData[name] || ''}
                onChange={(e) => handleInputChange(name, e.target.value, type)}
                disabled={!isEditable}
                placeholder={`Enter ${label.toLowerCase()}`}
              />
            )}

            {error && <span className="input-error">⚠ {error}</span>}
          </div>
        );
      })}

      <div className="grid-span-2" style={{ display: 'none' }} /> {/* Guard spacer */}
      
      <div className="modal-footer grid-span-2" style={{ padding: '1rem 0 0 0', background: 'transparent', border: 'none' }}>
        <button type="button" className="btn btn-secondary" onClick={onCancel}>
          Cancel
        </button>
        <button type="submit" className="btn btn-primary">
          Save Record
        </button>
      </div>
    </form>
  );
}
