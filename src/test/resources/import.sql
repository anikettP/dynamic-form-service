-- Seed Security Roles
INSERT INTO roles (id, role_name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles (id, role_name) VALUES (2, 'ROLE_USER');

-- Seed Security Users (Using BCrypt hashes for 'admin123' and 'user123')
INSERT INTO users (id, username, password, enabled, created_by, created_at) VALUES (1, 'admin', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHiM6Y9wQ6wWQ7Q9Y0mWv6tD8T6m2Q5u', true, 'system', CURRENT_TIMESTAMP);
INSERT INTO users (id, username, password, enabled, created_by, created_at) VALUES (2, 'user', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHiM6Y9wQ6wWQ7Q9Y0mWv6tD8T6m2Q5u', true, 'system', CURRENT_TIMESTAMP);

-- Associate Users with Roles
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);

-- Seed Employee Master
INSERT INTO employee_master (id, employee_id, force_no, employee_name, rank, unit, active, created_by, created_at) VALUES (1, 'CRPF001', 'CRPF001', 'Demo Employee', 'SI', 'Delhi', true, 'system', CURRENT_TIMESTAMP);

-- Seed Form Metadata
INSERT INTO form_metadata (id, form_name, description, active) VALUES (1, 'education', 'Education Records Form', true);
INSERT INTO form_metadata (id, form_name, description, active) VALUES (2, 'family', 'Dependent and Family Member Management Form', true);

-- Seed Form Field Metadata for Education
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order) VALUES (1, 'forceNo', 'Force Number', 'TEXT', true, true, true, 1);
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order) VALUES (1, 'name', 'Employee Name', 'TEXT', true, true, true, 2);
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order) VALUES (1, 'rank', 'Rank', 'TEXT', false, true, true, 3);
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order) VALUES (1, 'unit', 'Unit', 'TEXT', false, true, true, 4);
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order) VALUES (1, 'educationType', 'Education Type', 'TEXT', true, true, true, 5);
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order) VALUES (1, 'schoolName', 'School Name', 'TEXT', false, true, true, 6);
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order) VALUES (1, 'passingYear', 'Passing Year', 'DATE', true, true, true, 7);
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order) VALUES (1, 'examPassed', 'Exam Passed', 'TEXT', false, true, true, 8);
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order) VALUES (1, 'subject1', 'Subject 1', 'TEXT', true, true, true, 9);
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order) VALUES (1, 'subject2', 'Subject 2', 'TEXT', false, true, true, 10);
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order) VALUES (1, 'subject3', 'Subject 3', 'TEXT', false, true, true, 11);
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order) VALUES (1, 'startDate', 'Start Date', 'DATE', false, true, true, 12);
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order) VALUES (1, 'endDate', 'End Date', 'DATE', false, true, true, 13);
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order) VALUES (1, 'remarks', 'Remarks', 'TEXT', false, true, true, 14);

-- Seed Form Field Metadata for Family
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order, min_length, max_length, regex_pattern, dropdown_values) VALUES (2, 'forceNo', 'Force Number', 'TEXT', true, true, true, 1, NULL, NULL, NULL, NULL);
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order, min_length, max_length, regex_pattern, dropdown_values) VALUES (2, 'memberName', 'Member Name', 'TEXT', true, true, true, 2, NULL, NULL, NULL, NULL);
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order, min_length, max_length, regex_pattern, dropdown_values) VALUES (2, 'dob', 'Date of Birth', 'DATE', true, true, true, 3, NULL, NULL, NULL, NULL);
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order, min_length, max_length, regex_pattern, dropdown_values) VALUES (2, 'relationship', 'Relationship', 'TEXT', true, true, true, 4, NULL, NULL, NULL, 'Father,Mother,Son,Daughter');
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order, min_length, max_length, regex_pattern, dropdown_values) VALUES (2, 'memberType', 'Member Type', 'TEXT', false, true, true, 5, NULL, NULL, NULL, NULL);
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order, min_length, max_length, regex_pattern, dropdown_values) VALUES (2, 'employed', 'Employed', 'BOOLEAN', false, true, true, 6, NULL, NULL, NULL, NULL);
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order, min_length, max_length, regex_pattern, dropdown_values) VALUES (2, 'handicapped', 'Handicapped', 'BOOLEAN', false, true, true, 7, NULL, NULL, NULL, NULL);
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order, min_length, max_length, regex_pattern, dropdown_values) VALUES (2, 'disabilityPercentage', 'Disability Percentage', 'NUMBER', false, true, true, 8, NULL, NULL, NULL, NULL);
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order, min_length, max_length, regex_pattern, dropdown_values) VALUES (2, 'autisticMember', 'Autistic Member', 'BOOLEAN', false, true, true, 9, NULL, NULL, NULL, NULL);
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order, min_length, max_length, regex_pattern, dropdown_values) VALUES (2, 'aadhaarNumber', 'Aadhaar Number', 'TEXT', false, true, true, 10, 12, 12, '^\\d{12}$', NULL);
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order, min_length, max_length, regex_pattern, dropdown_values) VALUES (2, 'panNumber', 'PAN Number', 'TEXT', false, true, true, 11, 10, 10, '^[A-Z]{5}[0-9]{4}[A-Z]{1}$', NULL);
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order, min_length, max_length, regex_pattern, dropdown_values) VALUES (2, 'mobileNumber', 'Mobile Number', 'TEXT', false, true, true, 12, 10, 10, '^[6-9]\\d{9}$', NULL);
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order, min_length, max_length, regex_pattern, dropdown_values) VALUES (2, 'remarks', 'Remarks', 'TEXT', false, true, true, 13, NULL, NULL, NULL, NULL);

-- Restart sequences to prevent duplicate ID conflicts
ALTER TABLE roles ALTER COLUMN id RESTART WITH 100;
ALTER TABLE users ALTER COLUMN id RESTART WITH 100;
ALTER TABLE employee_master ALTER COLUMN id RESTART WITH 100;
ALTER TABLE form_metadata ALTER COLUMN id RESTART WITH 100;
ALTER TABLE form_field_metadata ALTER COLUMN id RESTART WITH 100;
