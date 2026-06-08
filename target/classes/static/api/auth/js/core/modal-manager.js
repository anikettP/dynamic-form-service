// Reusable Modal & Notification Manager
class NotificationManager {
    static showToast(message, type = 'info') {
        let container = document.getElementById('gov-toast-container');
        if (!container) {
            container = document.createElement('div');
            container.id = 'gov-toast-container';
            container.className = 'gov-toast-container';
            document.body.appendChild(container);
        }

        const toast = document.createElement('div');
        toast.className = `gov-toast ${type}`;

        let icon = 'fa-circle-info';
        if (type === 'success') icon = 'fa-circle-check text-success';
        if (type === 'error') icon = 'fa-triangle-exclamation text-danger';

        toast.innerHTML = `
            <i class="fa-solid ${icon} fs-5"></i>
            <div>${message}</div>
        `;

        container.appendChild(toast);

        // Auto remove toast
        setTimeout(() => {
            toast.style.animation = 'govToastSlide 0.25s cubic-bezier(0.16, 1, 0.3, 1) reverse';
            setTimeout(() => toast.remove(), 250);
        }, 4000);
    }

    static showAlert(containerId, message, type = 'danger') {
        const container = document.getElementById(containerId);
        if (!container) return;

        container.innerHTML = `
            <div class="gov-alert gov-alert-${type}">
                <i class="fa-solid fa-triangle-exclamation"></i>
                <div>${message}</div>
            </div>
        `;
        container.classList.remove('d-none');
    }

    static clearAlert(containerId) {
        const container = document.getElementById(containerId);
        if (container) {
            container.innerHTML = '';
            container.classList.add('d-none');
        }
    }
}

class ModalManager {
    static confirm(title, message, onConfirm) {
        // Build a temporary bootstrap modal
        let modalEl = document.getElementById('gov-confirm-modal');
        if (!modalEl) {
            modalEl = document.createElement('div');
            modalEl.id = 'gov-confirm-modal';
            modalEl.className = 'modal fade';
            modalEl.tabIndex = -1;
            modalEl.setAttribute('data-bs-backdrop', 'static');
            modalEl.innerHTML = `
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content" style="border-radius: 4px; border: 1px solid var(--gov-border);">
                        <div class="modal-header" style="border-bottom: 2px solid var(--gov-secondary); background-color: var(--gov-bg);">
                            <h5 class="modal-title fw-bold" style="color: var(--gov-primary); font-size: 1rem; text-transform: uppercase;">Confirm Action</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <p id="gov-confirm-modal-body" class="mb-0"></p>
                        </div>
                        <div class="modal-footer" style="border-top: 1px solid var(--gov-border);">
                            <button type="button" class="gov-btn gov-btn-secondary" data-bs-dismiss="modal">Cancel</button>
                            <button type="button" id="gov-confirm-modal-ok" class="gov-btn gov-btn-maroon">Confirm</button>
                        </div>
                    </div>
                </div>
            `;
            document.body.appendChild(modalEl);
        }

        modalEl.querySelector('.modal-title').innerText = title;
        modalEl.querySelector('#gov-confirm-modal-body').innerText = message;

        const bootstrapModal = new bootstrap.Modal(modalEl);
        
        const okBtn = modalEl.querySelector('#gov-confirm-modal-ok');
        // Clean previous listeners
        const newOkBtn = okBtn.cloneNode(true);
        okBtn.parentNode.replaceChild(newOkBtn, okBtn);

        newOkBtn.addEventListener('click', () => {
            bootstrapModal.hide();
            onConfirm();
        });

        bootstrapModal.show();
    }
}

window.NotificationManager = NotificationManager;
window.ModalManager = ModalManager;
