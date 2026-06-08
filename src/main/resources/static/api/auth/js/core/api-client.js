// Reusable API Client for Dynamic Form Service
class ApiClient {
    static getToken() {
        return localStorage.getItem('jwt_token');
    }

    static setToken(token) {
        localStorage.setItem('jwt_token', token);
    }

    static removeToken() {
        localStorage.removeItem('jwt_token');
    }

    static getUsername() {
        return localStorage.getItem('username');
    }

    static setUsername(username) {
        localStorage.setItem('username', username);
    }

    static getRoles() {
        return JSON.parse(localStorage.getItem('user_roles') || '[]');
    }

    static setRoles(roles) {
        localStorage.setItem('user_roles', JSON.stringify(roles));
    }

    static clearAuth() {
        this.removeToken();
        localStorage.removeItem('username');
        localStorage.removeItem('user_roles');
    }

    static async request(url, options = {}) {
        options.headers = options.headers || {};
        
        const token = this.getToken();
        if (token) {
            options.headers['Authorization'] = `Bearer ${token}`;
        }

        if (options.body && typeof options.body === 'object' && !(options.body instanceof FormData)) {
            options.headers['Content-Type'] = 'application/json';
            options.body = JSON.stringify(options.body);
        }

        try {
            const response = await fetch(url, options);

            if (response.status === 401) {
                this.clearAuth();
                // Custom event to notify that auth has expired (e.g. to toggle UI login modal/overlay)
                document.dispatchEvent(new CustomEvent('auth-expired'));
                throw new Error('Session expired. Please authenticate again.');
            }

            if (response.status === 204) {
                return null;
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
                        // Spring validation errors format
                        throw { status: response.status, validationErrors: errData.errors };
                    }
                    errorMsg = errData.message || errorMsg;
                } else {
                    errorMsg = await response.text();
                }
                throw new Error(errorMsg);
            }
        } catch (err) {
            throw err;
        }
    }

    static async get(url) {
        return this.request(url, { method: 'GET' });
    }

    static async post(url, body) {
        return this.request(url, { method: 'POST', body });
    }

    static async put(url, body) {
        return this.request(url, { method: 'PUT', body });
    }

    static async delete(url) {
        return this.request(url, { method: 'DELETE' });
    }
}

window.ApiClient = ApiClient;
