// Reusable Record Manager
class RecordManager {
    constructor(config = {}) {
        this.moduleName = config.moduleName;
        this.tableBodyId = config.tableBodyId || 'records-list';
        this.formId = config.formId || 'dynamic-record-form';
        this.formContainerId = config.formContainerId || 'dynamic-fields-container';
        this.formCardId = config.formCardId || 'dynamic-form-card';
        this.alertId = config.alertId || 'modal-error-alert';
        this.searchBarId = config.searchBarId || 'search-input';
        this.refreshBtnId = config.refreshBtnId || 'btn-refresh';
        this.addBtnId = config.addBtnId || 'btn-add-record';

        this.records = [];
        this.editingRecordId = null;
        this.currentForceNo = null;
        this.currentEmployee = null;

        this.init();
    }

    init() {
        const refreshBtn = document.getElementById(this.refreshBtnId);
        if (refreshBtn) {
            refreshBtn.addEventListener('click', () => this.loadRecords());
        }

        const searchBar = document.getElementById(this.searchBarId);
        if (searchBar) {
            searchBar.addEventListener('input', (e) => this.filterRecords(e.target.value));
        }

        const addBtn = document.getElementById(this.addBtnId);
        if (addBtn) {
            addBtn.addEventListener('click', () => this.openAddForm());
        }

        const form = document.getElementById(this.formId);
        if (form) {
            form.addEventListener('submit', (e) => this.handleFormSubmit(e));
        }

        const cancelBtn = form ? form.querySelector('.btn-cancel') : null;
        if (cancelBtn) {
            cancelBtn.addEventListener('click', () => this.closeForm());
        }
    }

    setEmployeeContext(employee) {
        this.currentForceNo = employee ? employee.forceNo : null;
        this.currentEmployee = employee;

        // Toggle add button depending on whether employee is searched
        const addBtn = document.getElementById(this.addBtnId);
        if (addBtn) {
            const hasCreate = ApiClient.getRoles().includes('CREATE') || ApiClient.getRoles().includes('ROLE_ADMIN');
            addBtn.style.display = (employee && hasCreate) ? 'block' : 'none';
        }

        // Auto open dynamic form on search
        if (employee) {
            this.openAddForm();
        } else {
            this.closeForm();
        }
    }

    async loadRecords() {
        const tbody = document.getElementById(this.tableBodyId);
        if (tbody) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="15" class="text-center py-4">
                        <div class="gov-loader">
                            <div class="gov-spinner"></div>
                            <span>Loading records from portal...</span>
                        </div>
                    </td>
                </tr>
            `;
        }

        try {
            this.records = await ApiClient.get(`/api/${this.moduleName}`);
            this.renderTable(this.records);
        } catch (err) {
            console.error('Error loading records:', err);
            if (tbody) {
                tbody.innerHTML = `
                    <tr>
                        <td colspan="15" class="text-center py-4 text-danger">
                            <i class="fa-solid fa-triangle-exclamation me-1"></i> Failed to retrieve records.
                        </td>
                    </tr>
                `;
            }
        }
    }

    renderTable(recordsList) {
        const tbody = document.getElementById(this.tableBodyId);
        if (!tbody) return;

        tbody.innerHTML = '';

        if (!recordsList || recordsList.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="15" class="text-center py-4 text-muted">No records found.</td>
                </tr>
            `;
            return;
        }

        const hasUpdate = ApiClient.getRoles().includes('UPDATE') || ApiClient.getRoles().includes('ROLE_ADMIN');
        const hasDelete = ApiClient.getRoles().includes('DELETE') || ApiClient.getRoles().includes('ROLE_ADMIN');

        recordsList.forEach(rec => {
            const tr = document.createElement('tr');
            
            if (this.moduleName === 'education') {
                const subjects = [rec.subject1, rec.subject2, rec.subject3].filter(Boolean).join(', ');
                const duration = (rec.start_date && rec.end_date) ? `${rec.start_date} to ${rec.end_date}` : 'N/A';
                const passingYearStr = rec.passing_year ? new Date(rec.passing_year).getFullYear() : 'N/A';
                
                tr.innerHTML = `
                    <td class="fw-bold text-nowrap">${rec.force_no}</td>
                    <td>${rec.education_type || 'N/A'}</td>
                    <td>${rec.school_name || 'N/A'}</td>
                    <td>${passingYearStr}</td>
                    <td>${rec.exam_passed || 'N/A'}</td>
                    <td class="text-truncate" style="max-width: 150px;" title="${subjects}">${subjects || 'N/A'}</td>
                    <td class="text-nowrap">${duration}</td>
                    <td class="text-truncate" style="max-width: 150px;" title="${rec.remarks || ''}">${rec.remarks || 'N/A'}</td>
                    <td class="text-nowrap">
                        <div class="d-flex gap-1">
                            <button class="gov-btn gov-btn-secondary py-1 px-2 btn-edit" data-id="${rec.id}">
                                <i class="fa-solid fa-edit"></i>
                            </button>
                            <button class="gov-btn gov-btn-danger py-1 px-2 btn-delete" data-id="${rec.id}">
                                <i class="fa-solid fa-trash"></i>
                            </button>
                        </div>
                    </td>
                `;
            } else if (this.moduleName === 'family') {
                const handicappedText = rec.handicapped ? `<span class="badge bg-danger">Yes (${rec.disability_percentage || 0}%)</span>` : 'No';
                const autisticText = rec.autistic_member ? 'Yes' : 'No';
                const employedText = rec.employed ? 'Yes' : 'No';

                tr.innerHTML = `
                    <td class="fw-bold text-nowrap">${rec.force_no}</td>
                    <td>${rec.member_name || 'N/A'}</td>
                    <td>${rec.relationship || 'N/A'}</td>
                    <td class="text-nowrap">${rec.dob || 'N/A'}</td>
                    <td>${rec.age !== null ? rec.age : 'N/A'}</td>
                    <td>${employedText}</td>
                    <td>${handicappedText}</td>
                    <td>${autisticText}</td>
                    <td>${rec.aadhaar_number || 'N/A'}</td>
                    <td>${rec.pan_number || 'N/A'}</td>
                    <td>${rec.mobile_number || 'N/A'}</td>
                    <td class="text-truncate" style="max-width: 120px;" title="${rec.remarks || ''}">${rec.remarks || 'N/A'}</td>
                    <td class="text-nowrap">
                        <div class="d-flex gap-1">
                            <button class="gov-btn gov-btn-secondary py-1 px-2 btn-edit" data-id="${rec.id}">
                                <i class="fa-solid fa-edit"></i>
                            </button>
                            <button class="gov-btn gov-btn-danger py-1 px-2 btn-delete" data-id="${rec.id}">
                                <i class="fa-solid fa-trash"></i>
                            </button>
                        </div>
                    </td>
                `;
            }

            const btnEdit = tr.querySelector('.btn-edit');
            const btnDel = tr.querySelector('.btn-delete');

            if (!hasUpdate) btnEdit.disabled = true;
            else btnEdit.addEventListener('click', () => this.openEditForm(rec));

            if (!hasDelete) btnDel.disabled = true;
            else btnDel.addEventListener('click', () => this.handleDelete(rec.id));

            tbody.appendChild(tr);
        });
    }

    filterRecords(query) {
        const q = query.toLowerCase();
        const filtered = this.records.filter(rec => {
            return (rec.force_no && rec.force_no.toLowerCase().includes(q)) ||
                   (this.moduleName === 'education' && (
                       (rec.education_type && rec.education_type.toLowerCase().includes(q)) ||
                       (rec.school_name && rec.school_name.toLowerCase().includes(q)) ||
                       (rec.exam_passed && rec.exam_passed.toLowerCase().includes(q))
                   )) ||
                   (this.moduleName === 'family' && (
                       (rec.member_name && rec.member_name.toLowerCase().includes(q)) ||
                       (rec.relationship && rec.relationship.toLowerCase().includes(q))
                   ));
        });
        this.renderTable(filtered);
    }

    openAddForm() {
        this.editingRecordId = null;
        NotificationManager.clearAlert(this.alertId);
        
        const card = document.getElementById(this.formCardId);
        if (!card) return;

        // Toggle title
        const title = card.querySelector('.gov-card-header h2');
        if (title) title.innerText = `Add New ${this.moduleName === 'education' ? 'Education Record' : 'Family Member'}`;

        // Initialize Dynamic Form fields
        const metadata = window.moduleMetadata;
        if (metadata) {
            // Setup pre-seeded values using currently searched employee
            const initialValues = {};
            if (this.currentEmployee) {
                initialValues['force_no'] = this.currentEmployee.forceNo;
                if (this.moduleName === 'education') {
                    initialValues['name'] = this.currentEmployee.employeeName;
                    initialValues['rank'] = this.currentEmployee.rank;
                    initialValues['unit'] = this.currentEmployee.unit;
                }
            }
            FormRenderer.render(this.formContainerId, metadata.fields, initialValues);
        }

        card.classList.remove('d-none');
        card.scrollIntoView({ behavior: 'smooth' });
    }

    openEditForm(record) {
        this.editingRecordId = record.id;
        NotificationManager.clearAlert(this.alertId);

        const card = document.getElementById(this.formCardId);
        if (!card) return;

        // Toggle title
        const title = card.querySelector('.gov-card-header h2');
        if (title) title.innerText = `Edit ${this.moduleName === 'education' ? 'Education Record' : 'Family Member Details'}`;

        // Prepopulate dynamic form fields
        const metadata = window.moduleMetadata;
        if (metadata) {
            FormRenderer.render(this.formContainerId, metadata.fields, record);
        }

        card.classList.remove('d-none');
        card.scrollIntoView({ behavior: 'smooth' });
    }

    closeForm() {
        this.editingRecordId = null;
        NotificationManager.clearAlert(this.alertId);
        const card = document.getElementById(this.formCardId);
        if (card) card.classList.add('d-none');
    }

    async handleFormSubmit(e) {
        e.preventDefault();
        NotificationManager.clearAlert(this.alertId);

        // Run validation rules
        const { isValid, errors } = FormRenderer.validateForm(this.formId);
        if (!isValid) {
            NotificationManager.showAlert(this.alertId, 'Please review the fields marked in red.', 'danger');
            return;
        }

        // Serialize Form
        const payload = FormRenderer.serialize(this.formId);
        
        // Always enforce active state
        payload['active'] = true;

        // Ensure force_no matches current context if applicable
        if (!payload['force_no'] && this.currentForceNo) {
            payload['force_no'] = this.currentForceNo;
        }

        const submitBtn = e.target.querySelector('button[type="submit"]');
        const origHtml = submitBtn.innerHTML;
        submitBtn.disabled = true;
        submitBtn.innerHTML = `<div class="gov-spinner d-inline-block me-1" style="width:14px; height:14px;"></div> Saving...`;

        try {
            let url = `/api/${this.moduleName}/dynamicForm`;
            let method = 'POST';

            if (this.editingRecordId) {
                url = `${url}/${this.editingRecordId}`;
                method = 'PUT';
            } else {
                url = `${url}/${payload['force_no']}`;
            }

            await ApiClient.request(url, {
                method: method,
                body: payload
            });

            NotificationManager.showToast(
                this.editingRecordId ? 'Record updated successfully!' : 'Record created successfully!', 
                'success'
            );

            this.closeForm();
            this.loadRecords();
        } catch (err) {
            console.error('Error saving record:', err);
            if (err.validationErrors) {
                FormRenderer.showValidationErrors(this.formId, err.validationErrors);
                NotificationManager.showAlert(this.alertId, 'Validation failed. Please correct the fields.', 'danger');
            } else {
                NotificationManager.showAlert(this.alertId, err.message || 'Saving failed.', 'danger');
            }
        } finally {
            submitBtn.disabled = false;
            submitBtn.innerHTML = origHtml;
        }
    }

    handleDelete(id) {
        ModalManager.confirm(
            'Confirm Deletion',
            'Are you sure you want to delete this record from the system (Soft Delete)?',
            async () => {
                try {
                    await ApiClient.delete(`/api/${this.moduleName}/${id}`);
                    NotificationManager.showToast('Record deleted successfully.', 'success');
                    this.loadRecords();
                } catch (err) {
                    NotificationManager.showToast(err.message || 'Deletion failed.', 'error');
                }
            }
        );
    }
}

window.RecordManager = RecordManager;
