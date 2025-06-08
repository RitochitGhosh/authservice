package com.airwaymanagement.authservice.services.implementations;

import com.airwaymanagement.authservice.model.entities.Role;
import com.airwaymanagement.authservice.model.entities.RoleName;
import com.airwaymanagement.authservice.repository.RoleRepository;
import com.airwaymanagement.authservice.repository.UserRepository;
import com.airwaymanagement.authservice.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public Optional<Role> findByName(RoleName rolename){
        return Optional.ofNullable(roleRepository.findByRoleName(rolename))
                .orElseThrow(() -> new RuntimeException("Role Not Found with name: " + rolename));
    }
}
