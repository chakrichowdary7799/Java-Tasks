-- Seed Mock Enterprise Profiles
INSERT INTO app_users (name, email, role, created_at) VALUES 
('Alice Smith', 'alice@enterprise.com', 'USER', CURRENT_TIMESTAMP()),
('Bob Jones', 'bob@enterprise.com', 'ADMIN', CURRENT_TIMESTAMP()),
('Charlie Brown', 'charlie@enterprise.com', 'USER', CURRENT_TIMESTAMP());

-- Seed Mock Active and Expired Subscriptions for Dashboard Metric tests
INSERT INTO subscriptions (user_id, plan_name, price, status, start_date) VALUES 
(1, 'Premium Monthly Plan', 49.99, 'ACTIVE', CURRENT_TIMESTAMP()),
(1, 'Basic Trial Plan', 0.00, 'EXPIRED', CURRENT_TIMESTAMP()),
(3, 'Enterprise Annual Plan', 999.99, 'ACTIVE', CURRENT_TIMESTAMP());

-- Seed System Audit Logging Entries
INSERT INTO audit_logs (action, executed_by, timestamp, details) VALUES 
('SYSTEM_INITIALIZATION', 'SYSTEM', CURRENT_TIMESTAMP(), 'Database populated with initial demo seed profiles.');