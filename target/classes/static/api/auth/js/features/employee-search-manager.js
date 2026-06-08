// Reusable Employee Search Manager
class EmployeeSearchManager {
    constructor(config = {}) {
        this.searchInputId = config.searchInputId || 'force-no-search-input';
        this.searchBtnId = config.searchBtnId || 'force-no-search-btn';
        this.detailsCardId = config.detailsCardId || 'employee-details-card';
        this.loaderId = config.loaderId || 'search-loader';
        this.onSuccess = config.onSuccess || (() => {});
        this.onClear = config.onClear || (() => {});

        this.init();
    }

    init() {
        const btn = document.getElementById(this.searchBtnId);
        if (btn) {
            btn.addEventListener('click', () => this.search());
        }

        const input = document.getElementById(this.searchInputId);
        if (input) {
            input.addEventListener('keypress', (e) => {
                if (e.key === 'Enter') {
                    e.preventDefault();
                    this.search();
                }
            });
        }
    }

    async search() {
        const input = document.getElementById(this.searchInputId);
        const forceNo = input ? input.value.trim() : '';

        if (!forceNo) {
            NotificationManager.showToast('Please enter a Force Number.', 'error');
            return;
        }

        this.showLoader(true);
        this.clearDetails();

        try {
            const employee = await ApiClient.post(`/api/employee/search/${forceNo}`);
            this.showLoader(false);

            if (employee && employee.employeeName) {
                this.displayDetails(employee);
                this.onSuccess(employee);
            } else {
                NotificationManager.showToast('No active employee found with this Force Number.', 'error');
                this.onClear();
            }
        } catch (err) {
            this.showLoader(false);
            NotificationManager.showToast(err.message || 'Verification failed.', 'error');
            this.onClear();
        }
    }

    displayDetails(emp) {
        const card = document.getElementById(this.detailsCardId);
        if (!card) return;

        card.classList.remove('d-none');
        
        const setVal = (selector, val) => {
            const el = card.querySelector(selector);
            if (el) el.innerText = val || 'N/A';
        };

        setVal('.emp-force-no', emp.forceNo);
        setVal('.emp-name', emp.employeeName);
        setVal('.emp-rank', emp.rank);
        setVal('.emp-unit', emp.unit);
        setVal('.emp-id', emp.employeeId);
        setVal('.emp-status', emp.active ? 'ACTIVE' : 'INACTIVE');
    }

    clearDetails() {
        const card = document.getElementById(this.detailsCardId);
        if (card) {
            card.classList.add('d-none');
        }
        this.onClear();
    }

    showLoader(show) {
        const loader = document.getElementById(this.loaderId);
        const btn = document.getElementById(this.searchBtnId);
        
        if (loader) {
            loader.className = show ? 'gov-loader ms-2' : 'gov-loader ms-2 d-none';
        }
        if (btn) {
            btn.disabled = show;
        }
    }
}

window.EmployeeSearchManager = EmployeeSearchManager;
