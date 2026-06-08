// Global Application State
const state = {
    token: localStorage.getItem('jwt_token'),
    user: localStorage.getItem('username'),
    roles: JSON.parse(localStorage.getItem('user_roles') || '[]'),
    formMetadata: null,
    records: [],
    editingRecordId: null
};

// Bootstrap Modal reference
let formModalInstance = null;

// Initialize on DOM load
document.addEventListener('DOMContentLoaded', () => {
    initAuth();
    
    // Attach general event listeners
    document.getElementById('login-form').addEventListener('submit', handleLogin);
    document.getElementById('logout-btn').addEventListener('click', handleLogout);
    document.getElementById('btn-add-record').addEventListener('click', () => openFormModal());
    document.getElementById('dynamic-record-form').addEventListener('submit', handleFormSubmit);
    document.getElementById('search-input').addEventListener('input', handleSearch);
});

/* ================= AUTHENTICATION & INITIALIZATION ================= */

function initAuth() {
    const loginContainer = document.getElementById('login-container');
    const appContainer = document.getElementById('app-container');

    if (state.token) {
        loginContainer.style.display = 'none';
        appContainer.style.display = 'block';
        document.getElementById('user-display').innerHTML = `<i class="fa-solid fa-user me-1"></i> ${state.user}`;
        
        // Setup permissions
        setupPermissions();
        
        // Load Application Data
        loadMetadataAndRecords();
    } else {
        loginContainer.style.display = 'flex';
        appContainer.style.display = 'none';
    }
}

function setupPermissions() {
    const hasCreate = state.roles.includes('CREATE') || state.roles.includes('ROLE_ADMIN');
    const btnAdd = document.getElementById('btn-add-record');
    if (btnAdd) {
        btnAdd.style.display = hasCreate ? 'block' : 'none';
    }
}

async function handleLogin(e) {
    e.preventDefault();
    const usernameInput = document.getElementById('username');
    const passwordInput = document.getElementById('password');

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                username: usernameInput.value,
                password: passwordInput.value
            })
        });

        if (!response.ok) {
            throw new Error('Invalid username or password');
        }

        const data = await response.json();
        
        // Save auth details
        localStorage.setItem('jwt_token', data.token);
        localStorage.setItem('username', data.username);
        localStorage.setItem('user_roles', JSON.stringify(data.roles));
        
        state.token = data.token;
        state.user = data.username;
        state.roles = data.roles;

        showToast('Successfully authenticated!', 'success');
        initAuth();
    } catch (err) {
        showToast(err.message, 'error');
    }
}

function handleLogout() {
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('username');
    localStorage.removeItem('user_roles');
    state.token = null;
    state.user = null;
    state.roles = [];
    showToast('Logged out successfully.', 'info');
    initAuth();
}

/* ================= API COMMUNICATION HELPER ================= */

async function fetchAPI(url, options = {}) {
    options.headers = options.headers || {};
    if (state.token) {
        options.headers['Authorization'] = `Bearer ${state.token}`;
    }
    
    if (options.body && typeof options.body === 'object' && !(options.body instanceof FormData)) {
        options.headers['Content-Type'] = 'application/json';
        options.body = JSON.stringify(options.body);
    }

    try {
        const response = await fetch(url, options);
        
        if (response.status === 401) {
            handleLogout();
            throw new Error('Session expired. Please log in again.');
        }

        if (response.status === 204) {
            return null; // No content
        }

        const contentType = response.headers.get('content-type');
        if (response.ok) {
            if (contentType && contentType.includes('application/json')) {
                return await response.json();
            }
            return await response.text();
        } else {
            let errorMsg = 'An unexpected error occurred.';
            if (contentType && contentType.includes('application/json')) {
                const errData = await response.json();
                if (errData.errors) {
                    throw { status: response.status, validationErrors: errData.errors };
                }
                errorMsg = errData.message || errorMsg;
            } else {
                errorMsg = await response.text();
            }
            throw new Error(errorMsg);
        }
    } catch (err) {
        if (err.validationErrors) throw err;
        showToast(err.message, 'error');
        throw err;
    }
}

/* ================= DATA LOADING ================= */

async function loadMetadataAndRecords() {
    try {
        // Fetch metadata schema
        state.formMetadata = await fetchAPI('/api/forms/family');
        
        // Fetch all active family records
        state.records = await fetchAPI('/api/family');
        
        renderRecordsTable(state.records);
    } catch (err) {
        console.error('Error loading initial data:', err);
    }
}

function renderRecordsTable(records) {
    const listContainer = document.getElementById('records-list');
    listContainer.innerHTML = '';

    if (records.length === 0) {
        listContainer.innerHTML = `
            <tr>
                <td colspan="12" class="text-center py-4 text-muted">No family members found.</td>
            </tr>
        `;
        return;
    }

    const hasUpdate = state.roles.includes('UPDATE') || state.roles.includes('ROLE_ADMIN');
    const hasDelete = state.roles.includes('DELETE') || state.roles.includes('ROLE_ADMIN');

    records.forEach(rec => {
        const row = document.createElement('tr');
        
        const forceNo = rec.force_no || '';
        const memberName = rec.member_name || '';
        
        // DOB & Age
        const dobStr = rec.dob || '';
        const age = rec.age !== null && rec.age !== undefined ? ` (${rec.age} yrs)` : '';
        const dobAndAge = dobStr + age;

        const relationship = rec.relationship || '';
        const memberType = rec.member_type || '';
        
        // Flags
        const employed = rec.employed ? '<span class="badge bg-success">Yes</span>' : '<span class="badge bg-secondary">No</span>';
        
        let handicappedStatus = '<span class="badge bg-secondary">No</span>';
        if (rec.handicapped) {
            const pct = rec.disability_percentage ? ` (${rec.disability_percentage}%)` : '';
            handicappedStatus = `<span class="badge bg-danger">Yes${pct}</span>`;
        }

        const aadhaar = rec.aadhaar_number || '';
        const pan = rec.pan_number || '';
        const mobile = rec.mobile_number || '';
        const remarks = rec.remarks || '';

        row.innerHTML = `
            <td class="fw-medium">${forceNo}</td>
            <td class="fw-semibold">${memberName}</td>
            <td>${dobAndAge}</td>
            <td>${relationship}</td>
            <td>${memberType}</td>
            <td>${employed}</td>
            <td>${handicappedStatus}</td>
            <td>${aadhaar}</td>
            <td>${pan}</td>
            <td>${mobile}</td>
            <td class="text-truncate" style="max-width: 150px;">${remarks}</td>
            <td>
                <div class="d-flex gap-2">
                    <button class="btn btn-sm btn-outline-primary py-1 px-2 btn-edit" style="border-radius: 8px;" data-id="${rec.id}">
                        <i class="fa-solid fa-pen-to-square"></i>
                    </button>
                    <button class="btn btn-sm btn-outline-danger py-1 px-2 btn-delete" style="border-radius: 8px;" data-id="${rec.id}">
                        <i class="fa-solid fa-trash-can"></i>
                    </button>
                </div>
            </td>
        `;

        const btnEdit = row.querySelector('.btn-edit');
        const btnDel = row.querySelector('.btn-delete');

        if (!hasUpdate) {
            btnEdit.setAttribute('disabled', 'true');
        } else {
            btnEdit.addEventListener('click', () => openFormModal(rec.id));
        }

        if (!hasDelete) {
            btnDel.setAttribute('disabled', 'true');
        } else {
            btnDel.addEventListener('click', () => handleDeleteRecord(rec.id));
        }

        listContainer.appendChild(row);
    });
}

function handleSearch(e) {
    const query = e.target.value.toLowerCase();
    const filtered = state.records.filter(rec => {
        return (rec.force_no && rec.force_no.toLowerCase().includes(query)) ||
               (rec.member_name && rec.member_name.toLowerCase().includes(query)) ||
               (rec.relationship && rec.relationship.toLowerCase().includes(query)) ||
               (rec.member_type && rec.member_type.toLowerCase().includes(query)) ||
               (rec.remarks && rec.remarks.toLowerCase().includes(query));
    });
    renderRecordsTable(filtered);
}

/* ================= DYNAMIC FORM GENERATION & INTERACTION ================= */

function openFormModal(id = null) {
    state.editingRecordId = id;
    const alertContainer = document.getElementById('modal-error-alert');
    alertContainer.classList.add('d-none');
    
    const modalLabel = document.getElementById('formModalLabel');
    modalLabel.innerHTML = id ? '<i class="fa-solid fa-pen-to-square text-teal me-2"></i>Edit Family Member' : '<i class="fa-solid fa-plus text-teal me-2"></i>Add Family Member';

    renderDynamicFields(state.formMetadata.fields);

    if (id) {
        const record = state.records.find(r => r.id === id);
        if (record) {
            populateForm(record);
        }
    }

    if (!formModalInstance) {
        formModalInstance = new bootstrap.Modal(document.getElementById('formModal'));
    }
    formModalInstance.show();
}

function renderDynamicFields(fields) {
    const container = document.getElementById('dynamic-fields-container');
    container.innerHTML = '';

    const sortedFields = [...fields].sort((a, b) => a.display_order - b.display_order);

    sortedFields.forEach(field => {
        if (field.visible === false) return;

        const colDiv = document.createElement('div');
        colDiv.className = 'col-md-6 mb-3 fade-in';
        colDiv.id = `field-group-${field.field_name}`;

        const label = document.createElement('label');
        label.className = 'form-label form-label-custom';
        label.setAttribute('for', `input-${field.field_name}`);
        label.innerHTML = field.label;
        if (field.required) {
            label.innerHTML += ' <span class="required-indicator">*</span>';
        }
        colDiv.appendChild(label);

        const wrapper = document.createElement('div');
        wrapper.className = 'input-group';
        
        let inputElement = null;

        if (field.field_type === 'BOOLEAN') {
            inputElement = document.createElement('select');
            inputElement.className = 'form-select form-control-custom';
            inputElement.innerHTML = `
                <option value="">-- Select Option --</option>
                <option value="true">Yes</option>
                <option value="false">No</option>
            `;
        } else {
            inputElement = document.createElement('input');
            inputElement.className = 'form-control form-control-custom';
            
            if (field.field_type === 'NUMBER') {
                inputElement.type = 'number';
            } else if (field.field_type === 'DATE') {
                inputElement.type = 'date';
            } else {
                inputElement.type = 'text';
            }
        }

        inputElement.id = `input-${field.field_name}`;
        inputElement.name = field.field_name;
        inputElement.style.borderRadius = '12px';
        
        if (field.editable === false) {
            inputElement.setAttribute('readonly', 'true');
        }

        // Store constraints in dataset
        if (field.min_length) inputElement.dataset.minLength = field.min_length;
        if (field.max_length) inputElement.dataset.maxLength = field.max_length;
        if (field.regex_pattern) inputElement.dataset.regex = field.regex_pattern;
        inputElement.dataset.required = field.required;
        inputElement.dataset.label = field.label;

        wrapper.appendChild(inputElement);
        colDiv.appendChild(wrapper);

        const feedback = document.createElement('div');
        feedback.className = 'invalid-feedback';
        feedback.id = `feedback-${field.field_name}`;
        colDiv.appendChild(feedback);

        container.appendChild(colDiv);

        // Bind dependency handling
        if (field.depends_on_field) {
            setTimeout(() => {
                evaluateDependency(field);
            }, 0);
        }
    });

    // Setup live change events for dependency targets
    sortedFields.forEach(field => {
        const input = document.getElementById(`input-${field.field_name}`);
        if (input) {
            input.addEventListener('change', () => {
                sortedFields.forEach(depField => {
                    if (depField.depends_on_field === field.field_name) {
                        evaluateDependency(depField);
                    }
                });
            });
        }
    });
}

function evaluateDependency(field) {
    const dependTarget = document.getElementById(`input-${field.depends_on_field}`);
    const currentGroup = document.getElementById(`field-group-${field.field_name}`);
    const currentInput = document.getElementById(`input-${field.field_name}`);
    
    if (!dependTarget || !currentGroup || !currentInput) return;

    const targetVal = dependTarget.value;
    const expectedVal = field.depends_on_value;

    // Matches if value matches expected dependency condition
    const isMet = String(targetVal).toLowerCase() === String(expectedVal).toLowerCase();

    if (isMet) {
        currentGroup.style.display = 'block';
        currentInput.removeAttribute('disabled');
        // Add dynamic validation: if dependency met, require it!
        currentInput.dataset.required = 'true';
        
        // Re-append asterisk to label if required
        const label = currentGroup.querySelector('label');
        if (label && !label.querySelector('.required-indicator')) {
            label.innerHTML += ' <span class="required-indicator">*</span>';
        }
    } else {
        currentGroup.style.display = 'none';
        currentInput.setAttribute('disabled', 'true');
        currentInput.dataset.required = 'false';
        currentInput.value = '';
        currentInput.classList.remove('is-invalid');
        
        // Remove required asterisk
        const label = currentGroup.querySelector('label');
        if (label) {
            const ast = label.querySelector('.required-indicator');
            if (ast) ast.remove();
        }
    }
}

function populateForm(record) {
    state.formMetadata.fields.forEach(field => {
        const input = document.getElementById(`input-${field.field_name}`);
        if (input) {
            const value = record[field.field_name];
            if (value !== undefined && value !== null) {
                if (field.field_type === 'BOOLEAN') {
                    input.value = String(value);
                } else {
                    input.value = value;
                }
                input.dispatchEvent(new Event('change'));
            }
        }
    });
}

/* ================= FORM SUBMISSION & VALIDATION ================= */

async function handleFormSubmit(e) {
    e.preventDefault();
    
    const alertContainer = document.getElementById('modal-error-alert');
    alertContainer.classList.add('d-none');

    const formElements = document.getElementById('dynamic-record-form').elements;
    let clientErrors = {};

    // Validate each rendered input
    for (let element of formElements) {
        if (!element.name || element.disabled) continue;

        const isRequired = element.dataset.required === 'true';
        const label = element.dataset.label;
        const val = element.value.trim();
        const minL = element.dataset.minLength;
        const maxL = element.dataset.maxLength;
        const regex = element.dataset.regex;

        // Required Check
        if (isRequired && !val) {
            clientErrors[element.name] = `${label} is required`;
            continue;
        }

        if (!val) continue;

        // Length Check
        if (minL && val.length < parseInt(minL)) {
            clientErrors[element.name] = `${label} must be at least ${minL} characters`;
            continue;
        }
        if (maxL && val.length > parseInt(maxL)) {
            clientErrors[element.name] = `${label} must not exceed ${maxL} characters`;
            continue;
        }

        // Regex check
        if (regex) {
            const re = new RegExp(regex);
            if (!re.test(val)) {
                clientErrors[element.name] = `${label} format is invalid`;
                continue;
            }
        }
    }

    if (Object.keys(clientErrors).length > 0) {
        showValidationErrors(clientErrors);
        return;
    }

    // Construct request payload
    const payload = {};
    for (let element of formElements) {
        if (!element.name || element.disabled) continue;
        
        let val = element.value.trim();
        if (element.type === 'number') {
            val = val ? parseFloat(val) : null;
        } else if (element.tagName === 'SELECT' && (val === 'true' || val === 'false')) {
            val = val === 'true';
        } else {
            val = val === '' ? null : val;
        }
        
        payload[element.name] = val;
    }

    try {
        const saveButton = document.getElementById('btn-save-record');
        saveButton.disabled = true;
        saveButton.innerHTML = '<span class="spinner-border spinner-border-sm me-1" role="status" aria-hidden="true"></span>Saving...';

        let url = '/api/family';
        let method = 'POST';

        if (state.editingRecordId) {
            url = `/api/family/${state.editingRecordId}`;
            method = 'PUT';
        }

        await fetchAPI(url, {
            method: method,
            body: payload
        });

        saveButton.disabled = false;
        saveButton.innerHTML = '<i class="fa-solid fa-floppy-disk me-2"></i>Save Record';

        showToast(state.editingRecordId ? 'Record updated successfully!' : 'Record created successfully!', 'success');
        
        formModalInstance.hide();
        loadMetadataAndRecords();
    } catch (err) {
        const saveButton = document.getElementById('btn-save-record');
        saveButton.disabled = false;
        saveButton.innerHTML = '<i class="fa-solid fa-floppy-disk me-2"></i>Save Record';

        if (err.validationErrors) {
            showValidationErrors(err.validationErrors);
        } else {
            document.getElementById('modal-error-text').innerText = err.message;
            alertContainer.classList.remove('d-none');
        }
    }
}

function showValidationErrors(errors) {
    const inputs = document.getElementById('dynamic-record-form').querySelectorAll('.form-control-custom, .form-select');
    inputs.forEach(inp => inp.classList.remove('is-invalid'));

    Object.keys(errors).forEach(key => {
        // Map camelCase keys returned by business rules to snake_case field ids
        const formattedKey = key.replace(/([A-Z])/g, "_$1").toLowerCase();
        
        const input = document.getElementById(`input-${formattedKey}`) || document.getElementById(`input-${key}`);
        const feedback = document.getElementById(`feedback-${formattedKey}`) || document.getElementById(`feedback-${key}`);
        
        if (input && feedback) {
            input.classList.add('is-invalid');
            feedback.innerText = errors[key];
        }
    });

    const alertContainer = document.getElementById('modal-error-alert');
    document.getElementById('modal-error-text').innerText = 'Validation failed. Please correct the fields marked in red.';
    alertContainer.classList.remove('d-none');
}

/* ================= SOFT DELETION ================= */

async function handleDeleteRecord(id) {
    if (!confirm('Are you sure you want to delete this family member record?')) {
        return;
    }

    try {
        await fetchAPI(`/api/family/${id}`, {
            method: 'DELETE'
        });
        showToast('Record deleted successfully (soft delete).', 'success');
        loadMetadataAndRecords();
    } catch (err) {
        console.error('Delete failed:', err);
    }
}

/* ================= TOAST NOTIFICATION HANDLER ================= */

function showToast(message, type = 'info') {
    const container = document.getElementById('toast-container');
    const toast = document.createElement('div');
    toast.className = `toast-custom ${type}`;
    
    let icon = 'fa-circle-info';
    if (type === 'success') icon = 'fa-circle-check text-success';
    if (type === 'error') icon = 'fa-triangle-exclamation text-danger';

    toast.innerHTML = `
        <i class="fa-solid ${icon} me-2 fs-5"></i>
        <div class="fw-semibold">${message}</div>
    `;

    container.appendChild(toast);

    setTimeout(() => {
        toast.style.animation = 'slideIn 0.3s ease-out reverse';
        setTimeout(() => toast.remove(), 300);
    }, 4000);
}
