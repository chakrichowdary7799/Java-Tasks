package com.enterprise.security.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.enterprise.security.entity.Role;
public interface RoleRepository extends JpaRepository<Role, Long> {
}
