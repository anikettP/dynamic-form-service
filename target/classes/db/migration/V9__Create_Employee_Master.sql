CREATE TABLE employee_master
(
    id            BIGSERIAL PRIMARY KEY,

    force_no      VARCHAR(50)                           NOT NULL,
    employee_name VARCHAR(255)                          NOT NULL,
    rank          VARCHAR(100),
    unit          VARCHAR(255),

    active        BOOLEAN                  DEFAULT TRUE NOT NULL,

    created_by    VARCHAR(100),
    created_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    updated_by    VARCHAR(100),
    updated_at    TIMESTAMP WITH TIME ZONE,

    deleted_by    VARCHAR(100),
    deleted_at    TIMESTAMP WITH TIME ZONE,

    employee_id   VARCHAR(50)                           NOT NULL
);

CREATE UNIQUE INDEX idx_employee_force_no
    ON employee_master (force_no);

CREATE UNIQUE INDEX idx_employee_employee_id
    ON employee_master (employee_id);

INSERT INTO employee_master (force_no,
                             employee_name,
                             rank,
                             unit,
                             employee_id,
                             active)
VALUES ('CRPF001',
        'Demo Employee',
        'Inspector',
        'Delhi Unit',
        'EMP001',
        TRUE);