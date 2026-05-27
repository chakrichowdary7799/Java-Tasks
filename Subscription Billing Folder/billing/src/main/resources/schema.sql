DROP TABLE IF EXISTS audit_logs;
DROP TABLE IF EXISTS subscriptions;
DROP TABLE IF EXISTS app_users;

CREATE TABLE app_users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE subscriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    plan_name VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL,
    status VARCHAR(50) NOT NULL,
    start_date TIMESTAMP NOT NULL,
    CONSTRAINT fk_subscription_user FOREIGN KEY (user_id) REFERENCES app_users(id) ON DELETE CASCADE
);

CREATE TABLE audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    action VARCHAR(255) NOT NULL,
    executed_by VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    details VARCHAR(255)
);