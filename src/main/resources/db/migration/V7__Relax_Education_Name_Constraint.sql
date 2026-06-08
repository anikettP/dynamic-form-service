-- V7: Relax NOT NULL constraint on education_record.name, rank, unit columns
-- These fields are commented out in EducationRecord.java entity,
-- so the JPA entity never writes to them. Without this fix, every INSERT fails
-- with "null value in column 'name' violates not-null constraint".

ALTER TABLE dynamic_form.education_record
    ALTER COLUMN name DROP NOT NULL;

-- Also set a default so existing rows without a value are safe
ALTER TABLE dynamic_form.education_record
    ALTER COLUMN name SET DEFAULT '';
