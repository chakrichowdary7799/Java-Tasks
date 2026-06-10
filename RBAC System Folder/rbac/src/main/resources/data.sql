INSERT IGNORE INTO permissions (id, name) VALUES (1, 'READ_PRIVILEGE');
INSERT IGNORE INTO permissions (id, name) VALUES (2, 'WRITE_PRIVILEGE');
INSERT IGNORE INTO permissions (id, name) VALUES (3, 'DELETE_PRIVILEGE');

INSERT IGNORE INTO roles (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT IGNORE INTO roles (id, name) VALUES (2, 'ROLE_USER');

INSERT IGNORE INTO roles_permissions (role_id, permission_id) VALUES (1, 1);
INSERT IGNORE INTO roles_permissions (role_id, permission_id) VALUES (1, 2);
INSERT IGNORE INTO roles_permissions (role_id, permission_id) VALUES (1, 3);
INSERT IGNORE INTO roles_permissions (role_id, permission_id) VALUES (2, 1);