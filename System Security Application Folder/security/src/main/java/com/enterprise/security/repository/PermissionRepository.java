package com.enterprise.security.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enterprise.security.entity.Permission;
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
