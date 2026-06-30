-- 1. Drop existing structural tables to allow clean initialization runs
DROP TABLE IF EXISTS order_discounts;
DROP TABLE IF EXISTS user_coupons;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS coupons;

-- 2. Core Engine Rules Definition Table
CREATE TABLE coupons (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    type VARCHAR(20) NOT NULL,
    discount_value DECIMAL(12, 2) NOT NULL,
    expiry_date DATETIME NULL,
    usage_limit INT NULL,
    INDEX idx_coupon_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Structural Per-User Multi-Redemption Guard Table
CREATE TABLE user_coupons (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    coupon_id BIGINT NOT NULL,
    used_flag BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (coupon_id) REFERENCES coupons(id) ON DELETE CASCADE,
    UNIQUE KEY uq_user_coupon (user_id, coupon_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Baseline Orders Structural Table
CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    total_amount DECIMAL(12, 2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Audit Trace Allocation Transaction Log Table
CREATE TABLE order_discounts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    coupon_id BIGINT NOT NULL,
    discount_applied DECIMAL(12, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (coupon_id) REFERENCES coupons(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. Seed Operational Test Records
INSERT INTO coupons (code, type, discount_value, expiry_date, usage_limit) VALUES 
('SAVE20', 'FLAT', 20.00, '2030-12-31 23:59:59', 100),
('PERCENT15', 'PERCENTAGE', 15.00, '2030-12-31 23:59:59', 500),
('BIGSPENDER', 'CONDITIONAL', 100.00, '2030-12-31 23:59:59', 10);

INSERT INTO orders (user_id, total_amount) VALUES 
(1001, 250.00),
(1002, 650.00);
