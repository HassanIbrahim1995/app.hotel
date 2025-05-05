-- Person table (parent class for Employee and Manager)
CREATE TABLE person (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address VARCHAR(200),
    date_of_birth DATE,
    person_type VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Employee table (extends Person)
CREATE TABLE employee (
    id INTEGER PRIMARY KEY REFERENCES person(id),
    employee_id VARCHAR(20) UNIQUE NOT NULL,
    hire_date DATE NOT NULL,
    department VARCHAR(50),
    position VARCHAR(50),
    hourly_rate DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);

-- Manager table (extends Person)
CREATE TABLE manager (
    id INTEGER PRIMARY KEY REFERENCES person(id),
    manager_id VARCHAR(20) UNIQUE NOT NULL,
    hire_date DATE NOT NULL,
    department VARCHAR(50),
    position VARCHAR(50),
    authorization_level VARCHAR(20) NOT NULL
);

-- Authentication table
CREATE TABLE auth_user (
    id INTEGER PRIMARY KEY REFERENCES person(id),
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    last_login TIMESTAMP,
    account_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
    account_non_locked BOOLEAN NOT NULL DEFAULT TRUE,
    credentials_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

-- Shift types (day, evening, night, etc.)
CREATE TABLE shift_type (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    hours DECIMAL(4, 2) NOT NULL,
    color VARCHAR(7) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Shifts assigned to employees
CREATE TABLE shift (
    id SERIAL PRIMARY KEY,
    employee_id INTEGER NOT NULL REFERENCES employee(id),
    shift_type_id INTEGER NOT NULL REFERENCES shift_type(id),
    date DATE NOT NULL,
    notes TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    created_by INTEGER NOT NULL REFERENCES manager(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Vacation requests
CREATE TABLE vacation_request (
    id SERIAL PRIMARY KEY,
    employee_id INTEGER NOT NULL REFERENCES employee(id),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    approved_by INTEGER REFERENCES manager(id),
    approved_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Notifications for shift assignments, changes, etc.
CREATE TABLE notification (
    id SERIAL PRIMARY KEY,
    person_id INTEGER NOT NULL REFERENCES person(id),
    title VARCHAR(100) NOT NULL,
    message TEXT NOT NULL,
    read BOOLEAN NOT NULL DEFAULT FALSE,
    notification_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Insert default shift types
INSERT INTO shift_type (name, start_time, end_time, hours, color) VALUES
('Day Shift', '08:00:00', '16:00:00', 8.0, '#4CAF50'),
('Evening Shift', '16:00:00', '00:00:00', 8.0, '#2196F3'),
('Night Shift', '00:00:00', '08:00:00', 8.0, '#9C27B0');
