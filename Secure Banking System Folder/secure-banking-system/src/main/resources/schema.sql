CREATE TABLE IF NOT EXISTS accounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(50) NOT NULL UNIQUE,
    balance DECIMAL(19, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_reference VARCHAR(255) NOT NULL UNIQUE,
    source_account_number VARCHAR(50) NOT NULL,
    destination_account_number VARCHAR(50) NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    timestamp DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL
);