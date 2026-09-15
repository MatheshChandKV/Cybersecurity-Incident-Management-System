-- -------------------------------------------------
-- CIMS database creation and seed data
-- -------------------------------------------------
DROP DATABASE IF EXISTS cims;
CREATE DATABASE cims CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE cims;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(64) NOT NULL,      
    name VARCHAR(100) NOT NULL,
    role ENUM('USER','ANALYST','ADMIN') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE incidents (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    type ENUM('PHISHING','MALWARE','UNAUTHORIZED_ACCESS','DATA_BREACH','DENIAL_OF_SERVICE') NOT NULL,
    impact TINYINT NOT NULL,
    likelihood TINYINT NOT NULL,
    exposure TINYINT NOT NULL,
    risk_score INT NOT NULL,
    severity VARCHAR(10) NOT NULL,
    status ENUM('REPORTED','OPEN','INVESTIGATING','RESOLVED','CLOSED') NOT NULL,
    reported_by INT NOT NULL,
    assigned_to INT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    FOREIGN KEY (reported_by) REFERENCES users(id) ON DELETE RESTRICT,
    FOREIGN KEY (assigned_to) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE investigations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    incident_id INT NOT NULL,
    analyst_id INT NOT NULL,
    findings TEXT NOT NULL,
    actions TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (incident_id) REFERENCES incidents(id) ON DELETE CASCADE,
    FOREIGN KEY (analyst_id) REFERENCES users(id) ON DELETE RESTRICT
);

CREATE TABLE audit_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    action VARCHAR(100) NOT NULL,
    incident_id INT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT,
    FOREIGN KEY (incident_id) REFERENCES incidents(id) ON DELETE SET NULL
);

-- -------------------------------------------------
-- Seed data – demo accounts (passwords are SHA-256 hashes)
-- Passwords: admin123, analyst123, user123
-- -------------------------------------------------
INSERT INTO users (username, password, name, role) VALUES
('admin',   '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Admin User',   'ADMIN'),
('analyst', '20249749412d73a3f5799f6f1dcf910e7b4aa3ce4de133b1f8a63c044792a4e9', 'Analyst User', 'ANALYST'),
('user',    'e606e38b0d8c19b24cf0ee3808183162ea7cd63ff7912dbb22b5e803286b4446', 'Normal User',  'USER');

INSERT INTO incidents
(title, description, type, impact, likelihood, exposure,
 risk_score, severity, status, reported_by, assigned_to, created_at)
VALUES
('Phishing email received',
 'User clicked a suspicious link.',
 'PHISHING', 3, 4, 2, 24, 'LOW', 'REPORTED', 3, NULL, NOW()),
('Malware on workstation',
 'Detected ransomware on PC.',
 'MALWARE', 5, 5, 5, 125, 'CRITICAL', 'OPEN', 3, 2, NOW()),
('Unauthorized server access',
 'Unexpected SSH login detected.',
 'UNAUTHORIZED_ACCESS', 4, 3, 3, 36, 'MEDIUM', 'INVESTIGATING', 3, 2, NOW());
