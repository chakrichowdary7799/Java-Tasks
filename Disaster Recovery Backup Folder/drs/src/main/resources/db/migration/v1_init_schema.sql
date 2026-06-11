CREATE TABLE IF NOT EXISTS backup_metadata (
    id VARCHAR(36) PRIMARY KEY,
    type VARCHAR(20) NOT NULL,          -- FULL, INCREMENTAL, DIFFERENTIAL
    timestamp DATETIME NOT NULL,
    location VARCHAR(255) NOT NULL,     -- S3 URI or local path
    status VARCHAR(20) NOT NULL,        -- PENDING, IN_PROGRESS, COMPLETED, FAILED
    checksum VARCHAR(64),
    compressed_size_bytes BIGINT
);

CREATE TABLE IF NOT EXISTS restore_logs (
    id VARCHAR(36) PRIMARY KEY,
    backup_id VARCHAR(36) NOT NULL,
    status VARCHAR(20) NOT NULL,        -- IN_PROGRESS, SUCCESS, FAILED
    time DATETIME NOT NULL,
    failure_reason TEXT,
    FOREIGN KEY (backup_id) REFERENCES backup_metadata(id)
);