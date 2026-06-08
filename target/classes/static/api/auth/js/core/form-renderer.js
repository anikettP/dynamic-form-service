// Reusable Dynamic Form Renderer
class FormRenderer {
    static getKnownDropdownOptions(fieldName) {
        const options = {
            'education_type': [
                { value: 'Matriculation (10th)', label: 'Matriculation (10th)' },
                { value: 'Higher Secondary (12th)', label: 'Higher Secondary (12th)' },
                { value: 'Diploma', label: 'Diploma' },
                { value: 'Graduation', label: 'Graduation' },
                { value: 'Post Graduation', label: 'Post Graduation' },
                { value: 'Doctorate', label: 'Doctorate (Ph.D)' },
                { value: 'Others', label: 'Others' }
            ],
            'relationship': [
                { value: 'Spouse', label: 'Spouse' },
                { value: 'Son', label: 'Son' },
                { value: 'Daughter', label: 'Daughter' },
                { value: 'Father', label: 'Father' },
                { value: 'Mother', label: 'Mother' },
                { value: 'Brother', label: 'Brother' },
                { value: 'Sister', label: 'Sister' }
            ],
            'member_type': [
                { value: 'Dependent', label: 'Dependent' },
                { value: 'Non-Dependent', label: 'Non-Dependent' }
            ]
        };
        return options[fieldName] || null;
    }

    static render(containerId, fields, values = {}) {
        const container = document.getElementById(containerId);
        if (!container) return;

        container.innerHTML = '';
        const sortedFields = MetadataService.getFieldsSorted({ fields });

        sortedFields.forEach(field => {
            // Skip non-visible fields
            if (field.visible === false) return;

            const colDiv = document.createElement('div');
            colDiv.className = 'col-md-6 mb-3';
            colDiv.id = `group-${field.field_name}`;

            const label = document.createElement('label');
            label.className = 'gov-label d-block';
            label.setAttribute('for', `input-${field.field_name}`);
            label.innerHTML = field.label;
            if (field.required) {
                label.innerHTML += ' <span class="text-danger">*</span>';
            }
            colDiv.appendChild(label);

            let inputElement = null;
            const dropdownOptions = this.getKnownDropdownOptions(field.field_name);

            // Determine element type
            if (field.field_type === 'BOOLEAN') {
                inputElement = document.createElement('select');
                inputElement.className = 'form-select gov-form-control w-100';
                inputElement.innerHTML = `
                    <option value="">-- Select Option --</option>
                    <option value="true">Yes</option>
                    <option value="false">No</option>
                `;
            } else if (dropdownOptions) {
                inputElement = document.createElement('select');
                inputElement.className = 'form-select gov-form-control w-100';
                let selectHtml = `<option value="">-- Select ${field.label} --</option>`;
                dropdownOptions.forEach(opt => {
                    selectHtml += `<option value="${opt.value}">${opt.label}</option>`;
                });
                inputElement.innerHTML = selectHtml;
            } else if (field.field_name === 'remarks') {
                inputElement = document.createElement('textarea');
                inputElement.className = 'form-control gov-form-control w-100';
                inputElement.rows = 2;
            } else {
                inputElement = document.createElement('input');
                inputElement.className = 'form-control gov-form-control w-100';
                
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

            // Apply read-only logic
            if (field.editable === false) {
                inputElement.setAttribute('readonly', 'true');
                if (inputElement.tagName === 'SELECT') {
                    inputElement.setAttribute('disabled', 'true');
                }
            }

            // Set constraints on dataset
            if (field.min_length) inputElement.dataset.minLength = field.min_length;
            if (field.max_length) inputElement.dataset.maxLength = field.max_length;
            if (field.regex_pattern) inputElement.dataset.regex = field.regex_pattern;
            inputElement.dataset.required = field.required;
            inputElement.dataset.label = field.label;

            // Populate initial value if exists
            const val = values[field.field_name];
            if (val !== undefined && val !== null) {
                inputElement.value = String(val);
            }

            colDiv.appendChild(inputElement);

            // Add validation feedback element
            const feedback = document.createElement('div');
            feedback.className = 'invalid-feedback';
            feedback.id = `feedback-${field.field_name}`;
            colDiv.appendChild(feedback);

            container.appendChild(colDiv);
        });

        // Initialize dependencies
        sortedFields.forEach(field => {
            if (field.depends_on_field) {
                this.evaluateDependency(field);
                
                // Bind target change event
                const targetInput = document.getElementById(`input-${field.depends_on_field}`);
                if (targetInput) {
                    targetInput.addEventListener('change', () => this.evaluateDependency(field));
                }
            }
        });
    }

    static evaluateDependency(field) {
        const targetInput = document.getElementById(`input-${field.depends_on_field}`);
        const currentGroup = document.getElementById(`group-${field.field_name}`);
        const currentInput = document.getElementById(`input-${field.field_name}`);

        if (!targetInput || !currentGroup || !currentInput) return;

        const targetVal = targetInput.value;
        const expectedVal = field.depends_on_value;
        const isMet = String(targetVal).toLowerCase() === String(expectedVal).toLowerCase();

        if (isMet) {
            currentGroup.style.display = 'block';
            currentInput.removeAttribute('disabled');
            if (field.required) {
                currentInput.dataset.required = 'true';
            }
        } else {
            currentGroup.style.display = 'none';
            currentInput.setAttribute('disabled', 'true');
            currentInput.dataset.required = 'false';
            currentInput.value = '';
            currentInput.classList.remove('is-invalid');
        }
    }

    static validateForm(formId) {
        const form = document.getElementById(formId);
        if (!form) return { isValid: true, errors: {} };

        const elements = form.elements;
        const errors = {};
        
        // Clear previous states
        form.querySelectorAll('.gov-form-control, .form-select').forEach(inp => {
            inp.classList.remove('is-invalid');
        });

        for (let element of elements) {
            if (!element.name || element.disabled) continue;

            const isRequired = element.dataset.required === 'true';
            const label = element.dataset.label || element.name;
            const val = element.value.trim();
            const minL = element.dataset.minLength;
            const maxL = element.dataset.maxLength;
            const regex = element.dataset.regex;

            // Required check
            if (isRequired && !val) {
                errors[element.name] = `${label} is required`;
                continue;
            }

            if (!val) continue;

            // Length check
            if (minL && val.length < parseInt(minL)) {
                errors[element.name] = `${label} must be at least ${minL} characters`;
                continue;
            }
            if (maxL && val.length > parseInt(maxL)) {
                errors[element.name] = `${label} must not exceed ${maxL} characters`;
                continue;
            }

            // Regex check
            if (regex) {
                try {
                    const re = new RegExp(regex);
                    if (!re.test(val)) {
                        errors[element.name] = `${label} format is invalid`;
                        continue;
                    }
                } catch (e) {
                    console.error('Invalid regex pattern:', regex);
                }
            }
        }

        const isValid = Object.keys(errors).length === 0;
        
        if (!isValid) {
            this.showValidationErrors(formId, errors);
        }

        return { isValid, errors };
    }

    static showValidationErrors(formId, errors) {
        const form = document.getElementById(formId);
        if (!form) return;

        Object.keys(errors).forEach(key => {
            const input = form.querySelector(`#input-${key}`);
            const feedback = form.querySelector(`#feedback-${key}`);
            if (input && feedback) {
                input.classList.add('is-invalid');
                feedback.innerText = errors[key];
            }
        });
    }

    static serialize(formId) {
        const form = document.getElementById(formId);
        if (!form) return {};

        const elements = form.elements;
        const data = {};

        for (let element of elements) {
            if (!element.name || element.disabled) continue;

            let val = element.value.trim();
            if (element.type === 'number') {
                data[element.name] = val ? parseFloat(val) : null;
            } else if (element.tagName === 'SELECT' && (val === 'true' || val === 'false')) {
                data[element.name] = val === 'true';
            } else {
                data[element.name] = val === '' ? null : val;
            }
        }

        return data;
    }
}

window.FormRenderer = FormRenderer;
