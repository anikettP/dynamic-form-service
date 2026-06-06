const API_BASE = '';

const getHeaders = () => {
  const headers = {
    'Content-Type': 'application/json',
  };
  const token = localStorage.getItem('jwt_token');
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }
  return headers;
};

const handleResponse = async (response) => {
  if (response.status === 401) {
    // Session expired or unauthorized
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('username');
    localStorage.removeItem('roles');
    window.dispatchEvent(new Event('auth-change'));
  }
  
  if (!response.ok) {
    let errorData = {};
    try {
      errorData = await response.json();
    } catch (e) {
      errorData = { message: 'An unexpected error occurred' };
    }
    
    // Throw detailed API error
    const err = new Error(errorData.message || 'API Request failed');
    err.status = response.status;
    err.errors = errorData.errors || null; // For validation exceptions
    throw err;
  }
  
  if (response.status === 204) {
    return null;
  }
  return response.json();
};

export const api = {
  // Auth
  login: async (username, password) => {
    const response = await fetch(`${API_BASE}/api/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password }),
    });
    
    const data = await handleResponse(response);
    if (data && data.token) {
      localStorage.setItem('jwt_token', data.token);
      localStorage.setItem('username', data.username);
      localStorage.setItem('roles', JSON.stringify(data.roles || []));
      window.dispatchEvent(new Event('auth-change'));
    }
    return data;
  },
  
  logout: () => {
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('username');
    localStorage.removeItem('roles');
    window.dispatchEvent(new Event('auth-change'));
  },
  
  isAuthenticated: () => {
    return !!localStorage.getItem('jwt_token');
  },
  
  getUserInfo: () => {
    const username = localStorage.getItem('username') || '';
    let roles = [];
    try {
      roles = JSON.parse(localStorage.getItem('roles') || '[]');
    } catch (e) {
      roles = [];
    }
    return { username, roles };
  },
  
  isAdmin: () => {
    const { roles } = api.getUserInfo();
    return roles.includes('ROLE_ADMIN');
  },

  // Metadata
  getForms: async () => {
    const response = await fetch(`${API_BASE}/api/forms`, {
      method: 'GET',
      headers: getHeaders(),
    });
    return handleResponse(response);
  },
  
  getFormMetadata: async (formName) => {
    const response = await fetch(`${API_BASE}/api/forms/${formName}`, {
      method: 'GET',
      headers: getHeaders(),
    });
    return handleResponse(response);
  },
  
  getFormFields: async (formName) => {
    const response = await fetch(`${API_BASE}/api/forms/${formName}/fields`, {
      method: 'GET',
      headers: getHeaders(),
    });
    return handleResponse(response);
  },
  
  // Dynamic CRUD Endpoints
  // formName will be 'education' or 'family', matching backend controller base paths
  getRecords: async (formName) => {
    const response = await fetch(`${API_BASE}/api/${formName}`, {
      method: 'GET',
      headers: getHeaders(),
    });
    const res = await handleResponse(response);
    if (Array.isArray(res)) {
      return res.map(r => ({
        id: r.id,
        employeeId: r.employeeId,
        forceNo: r.employeeId,
        ...r.formData
      }));
    }
    return res;
  },
  
  getRecordById: async (formName, id) => {
    const response = await fetch(`${API_BASE}/api/${formName}/${id}`, {
      method: 'GET',
      headers: getHeaders(),
    });
    const res = await handleResponse(response);
    if (res) {
      return {
        id: res.id,
        employeeId: res.employeeId,
        forceNo: res.employeeId,
        ...res.formData
      };
    }
    return res;
  },
  
  createRecord: async (formName, data) => {
    const employeeId = data.employeeId || data.employee_id || data.forceNo || data.force_no || '';
    const formData = { ...data };
    delete formData.forceNo;
    delete formData.force_no;
    delete formData.id;
    delete formData.employeeId;
    delete formData.employee_id;

    const response = await fetch(`${API_BASE}/api/${formName}`, {
      method: 'POST',
      headers: getHeaders(),
      body: JSON.stringify({ employeeId, formData }),
    });
    const res = await handleResponse(response);
    return {
      id: res.id,
      employeeId: res.employeeId,
      forceNo: res.employeeId,
      ...res.formData
    };
  },
  
  updateRecord: async (formName, id, data) => {
    const employeeId = data.employeeId || data.employee_id || data.forceNo || data.force_no || '';
    const formData = { ...data };
    delete formData.forceNo;
    delete formData.force_no;
    delete formData.id;
    delete formData.employeeId;
    delete formData.employee_id;

    const response = await fetch(`${API_BASE}/api/${formName}/${id}`, {
      method: 'PUT',
      headers: getHeaders(),
      body: JSON.stringify({ employeeId, formData }),
    });
    const res = await handleResponse(response);
    return {
      id: res.id,
      employeeId: res.employeeId,
      forceNo: res.employeeId,
      ...res.formData
    };
  },
  
  deleteRecord: async (formName, id) => {
    const response = await fetch(`${API_BASE}/api/${formName}/${id}`, {
      method: 'DELETE',
      headers: getHeaders(),
    });
    return handleResponse(response);
  }
};
