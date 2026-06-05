DROP TABLE IF EXISTS user_roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS family_member CASCADE;
DROP TABLE IF EXISTS education_record CASCADE;
DROP TABLE IF EXISTS form_field_metadata CASCADE;
DROP TABLE IF EXISTS form_metadata CASCADE;

CREATE TABLE form_metadata (
    id BIGSERIAL PRIMARY KEY,
    form_name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    active BOOLEAN DEFAULT TRUE NOT NULL
);

CREATE TABLE form_field_metadata (
    id BIGSERIAL PRIMARY KEY,
    form_id BIGINT NOT NULL REFERENCES form_metadata(id) ON DELETE CASCADE,
    field_name VARCHAR(100) NOT NULL,
    label VARCHAR(100) NOT NULL,
    field_type VARCHAR(50) NOT NULL,
    required BOOLEAN DEFAULT FALSE NOT NULL,
    visible BOOLEAN DEFAULT TRUE NOT NULL,
    editable BOOLEAN DEFAULT TRUE NOT NULL,
    min_length INTEGER,
    max_length INTEGER,
    regex_pattern VARCHAR(500),
    display_order INTEGER,
    depends_on_field VARCHAR(100),
    depends_on_value VARCHAR(100),
    CONSTRAINT uq_form_field UNIQUE (form_id, field_name)
);
