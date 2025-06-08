package com.airwaymanagement.authservice.services;

import com.airwaymanagement.authservice.model.entities.Role;
import com.airwaymanagement.authservice.model.entities.RoleName;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(RoleName name);
}
