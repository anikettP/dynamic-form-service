-- Seed Form Metadata
INSERT INTO form_metadata (form_name, description, active) VALUES
('education', 'Education Records Form', true),
('family', 'Dependent and Family Member Management Form', true);

-- Seed Form Field Metadata for Education
-- Form ID for education is 1
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order)
VALUES
(1, 'force_no', 'Force Number', 'TEXT', true, true, true, 1),
(1, 'name', 'Employee Name', 'TEXT', true, true, true, 2),
(1, 'rank', 'Rank', 'TEXT', false, true, true, 3),
(1, 'unit', 'Unit', 'TEXT', false, true, true, 4),
(1, 'education_type', 'Education Type', 'TEXT', true, true, true, 5),
(1, 'school_name', 'School Name', 'TEXT', false, true, true, 6),
(1, 'passing_year', 'Passing Year', 'DATE', true, true, true, 7),
(1, 'exam_passed', 'Exam Passed', 'TEXT', false, true, true, 8),
(1, 'subject1', 'Subject 1', 'TEXT', true, true, true, 9),
(1, 'subject2', 'Subject 2', 'TEXT', false, true, true, 10),
(1, 'subject3', 'Subject 3', 'TEXT', false, true, true, 11),
(1, 'start_date', 'Start Date', 'DATE', false, true, true, 12),
(1, 'end_date', 'End Date', 'DATE', false, true, true, 13),
(1, 'remarks', 'Remarks', 'TEXT', false, true, true, 14);

-- Seed Form Field Metadata for Family
-- Form ID for family is 2
-- Aadhaar pattern: ^\d{12}$
-- PAN pattern: ^[A-Z]{5}[0-9]{4}[A-Z]{1}$
-- Mobile pattern: ^[6-9]\d{9}$
INSERT INTO form_field_metadata (form_id, field_name, label, field_type, required, visible, editable, display_order, min_length, max_length, regex_pattern, depends_on_field, depends_on_value)
VALUES
(2, 'force_no', 'Force Number', 'TEXT', true, true, true, 1, NULL, NULL, NULL, NULL, NULL),
(2, 'member_name', 'Member Name', 'TEXT', true, true, true, 2, NULL, NULL, NULL, NULL, NULL),
(2, 'dob', 'Date of Birth', 'DATE', true, true, true, 3, NULL, NULL, NULL, NULL, NULL),
(2, 'relationship', 'Relationship', 'TEXT', true, true, true, 4, NULL, NULL, NULL, NULL, NULL),
(2, 'member_type', 'Member Type', 'TEXT', false, true, true, 5, NULL, NULL, NULL, NULL, NULL),
(2, 'employed', 'Employed', 'BOOLEAN', false, true, true, 6, NULL, NULL, NULL, NULL, NULL),
(2, 'handicapped', 'Handicapped', 'BOOLEAN', false, true, true, 7, NULL, NULL, NULL, NULL, NULL),
(2, 'disability_percentage', 'Disability Percentage', 'NUMBER', false, true, true, 8, NULL, NULL, NULL, 'handicapped', 'true'),
(2, 'autistic_member', 'Autistic Member', 'BOOLEAN', false, true, true, 9, NULL, NULL, NULL, NULL, NULL),
(2, 'aadhaar_number', 'Aadhaar Number', 'TEXT', false, true, true, 10, 12, 12, '^\d{12}$', NULL, NULL),
(2, 'pan_number', 'PAN Number', 'TEXT', false, true, true, 11, 10, 10, '^[A-Z]{5}[0-9]{4}[A-Z]{1}$', NULL, NULL),
(2, 'mobile_number', 'Mobile Number', 'TEXT', false, true, true, 12, 10, 10, '^[6-9]\d{9}$', NULL, NULL),
(2, 'remarks', 'Remarks', 'TEXT', false, true, true, 13, NULL, NULL, NULL, NULL, NULL);

-- Seed Security Roles
INSERT INTO roles (id, role_name) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_USER');

-- Seed Security Users
-- Using their provided hash for admin ('admin123' / '$2a$10$7EqJtq98hPqEX7fNZaFWoOHiM6Y9wQ6wWQ7Q9Y0mWv6tD8T6m2Q5u')
INSERT INTO users (id, username, password, enabled, created_by) VALUES
(1, 'admin', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHiM6Y9wQ6wWQ7Q9Y0mWv6tD8T6m2Q5u', true, 'system'),
(2, 'user', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHiM6Y9wQ6wWQ7Q9Y0mWv6tD8T6m2Q5u', true, 'system');

-- Associate Users with Roles
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), -- admin has ROLE_ADMIN
(2, 2);
