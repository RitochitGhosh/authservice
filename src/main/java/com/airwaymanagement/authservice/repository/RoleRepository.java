package com.airwaymanagement.authservice.repository;

import com.airwaymanagement.authservice.model.entities.Role;
import com.airwaymanagement.authservice.model.entities.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(RoleName roleName);
}
