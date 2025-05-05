-- Create persons table (base for Employee and Manager)
CREATE TABLE persons (
    id BIGSERIAL PRIMARY KEY,
    person_type VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    middle_name VARCHAR(255),
    last_name VARCHAR(255) NOT NULL,
    street VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL,
    zip_code VARCHAR(20) NOT NULL,
    country VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(255) NOT NULL UNIQUE,
    date_of_birth DATE NOT NULL,
    national_id VARCHAR(255),
    location_id BIGINT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Create locations table
CREATE TABLE locations (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    street VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL,
    zip_code VARCHAR(20) NOT NULL,
    country VARCHAR(255),
    phone VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    description TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Create employees table
CREATE TABLE employees (
    id BIGINT PRIMARY KEY REFERENCES persons(id),
    hire_date DATE NOT NULL,
    termination_date DATE,
    employee_number VARCHAR(255) NOT NULL UNIQUE,
    position VARCHAR(255),
    department VARCHAR(255) NOT NULL,
    manager_id BIGINT
);

-- Create managers table
CREATE TABLE managers (
    id BIGINT PRIMARY KEY REFERENCES persons(id),
    hire_date DATE NOT NULL,
    termination_date DATE,
    manager_number VARCHAR(255) NOT NULL UNIQUE,
    manager_level VARCHAR(255) NOT NULL,
    department VARCHAR(255)
);

-- Create schedules table
CREATE TABLE schedules (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    employee_id BIGINT REFERENCES employees(id),
    manager_id BIGINT REFERENCES managers(id),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    notes TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT chk_one_person CHECK (
        (employee_id IS NULL AND manager_id IS NOT NULL) OR
        (employee_id IS NOT NULL AND manager_id IS NULL)
    )
);

-- Create shifts table
CREATE TABLE shifts (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL REFERENCES employees(id),
    schedule_id BIGINT NOT NULL REFERENCES schedules(id),
    shift_type VARCHAR(50) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    notes TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Create vacation_requests table
CREATE TABLE vacation_requests (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL REFERENCES employees(id),
    manager_id BIGINT REFERENCES managers(id),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    reason TEXT,
    manager_comments TEXT,
    reviewed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Create notifications table
CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL REFERENCES employees(id),
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    read BOOLEAN NOT NULL DEFAULT FALSE,
    email_sent BOOLEAN NOT NULL DEFAULT FALSE,
    read_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Add foreign key constraints
ALTER TABLE persons ADD CONSTRAINT fk_persons_location FOREIGN KEY (location_id) REFERENCES locations(id);
ALTER TABLE employees ADD CONSTRAINT fk_employees_manager FOREIGN KEY (manager_id) REFERENCES managers(id);

-- Create indexes for better performance
CREATE INDEX idx_persons_email ON persons(email);
CREATE INDEX idx_persons_location ON persons(location_id);
CREATE INDEX idx_employees_manager ON employees(manager_id);
CREATE INDEX idx_employees_department ON employees(department);
CREATE INDEX idx_shifts_employee ON shifts(employee_id);
CREATE INDEX idx_shifts_schedule ON shifts(schedule_id);
CREATE INDEX idx_shifts_time_range ON shifts(start_time, end_time);
CREATE INDEX idx_vacation_requests_employee ON vacation_requests(employee_id);
CREATE INDEX idx_vacation_requests_manager ON vacation_requests(manager_id);
CREATE INDEX idx_vacation_requests_status ON vacation_requests(status);
CREATE INDEX idx_vacation_requests_date_range ON vacation_requests(start_date, end_date);
CREATE INDEX idx_notifications_employee ON notifications(employee_id);
CREATE INDEX idx_notifications_read ON notifications(read);
