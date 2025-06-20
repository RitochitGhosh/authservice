package com.airwaymanagement.authservice.services.implementations;

import com.airwaymanagement.authservice.model.dtos.requests.Signup;
import com.airwaymanagement.authservice.model.dtos.requests.StaffSignup;
import com.airwaymanagement.authservice.model.entities.RoleName;
import com.airwaymanagement.authservice.model.entities.Staff;
import com.airwaymanagement.authservice.model.entities.User;
import com.airwaymanagement.authservice.repository.StaffRepository;
import com.airwaymanagement.authservice.repository.UserRepository;
import com.airwaymanagement.authservice.security.jsonwebtokens.TokenProvider;
import com.airwaymanagement.authservice.security.usersecurity.UserDetailService;
import com.airwaymanagement.authservice.services.RoleService;
import com.airwaymanagement.authservice.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StaffRepository staffRepository;
    private final TokenProvider tokenProvider; // Currently not used.
    private final UserDetailService userDetailService; // Currently not used.
    private final ModelMapper modelMapper;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           StaffRepository staffRepository,
                           PasswordEncoder passwordEncoder,
                           TokenProvider tokenProvider,
                           UserDetailService userDetailsService,
                           ModelMapper modelMapper,
                           RoleService roleService
    ) {
        this.userRepository = userRepository;
        this.staffRepository = staffRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.userDetailService = userDetailsService;
        this.modelMapper = modelMapper;
        this.roleService = roleService;
    }

    public Mono<User> register(Signup signup) {
        return Mono.defer(() -> {
            if (existsByUsername(signup.getUserName())) {
                return Mono.error(new RuntimeException("The username " + signup.getUserName() + " is existed, please try again."));
            } else if (existsByEmail(signup.getEmail())) {
                return Mono.error(new RuntimeException("The email " + signup.getEmail() + " is existed, please try again."));
            }

            User user;

            try {
                user = modelMapper.map(signup, User.class);
                user.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse(signup.getDateOfBirth()));
                user.setPassword(passwordEncoder.encode(signup.getPassword()));
                user.setRoles(signup.getRoles()
                        .stream()
                        .map(role -> roleService.findByName(mapToRoleName(role))
                                .orElseThrow(() -> new RuntimeException("Role not found in the database.")))
                        .collect(Collectors.toSet()));

                userRepository.save(user);

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }


            return Mono.just(user);
        });
    }

    public Mono<User> registerStaff(StaffSignup signup) {
        return Mono.defer(() -> {
            if (existsByUsername(signup.getUserName())) {
                return Mono.error(new RuntimeException("The username " + signup.getUserName() + " is existed, please try again."));
            } else if (existsByEmail(signup.getEmail())) {
                return Mono.error(new RuntimeException("The email " + signup.getEmail() + " is existed, please try again."));
            }

            User user;

            try {
                user = modelMapper.map(signup, User.class);
                user.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse(signup.getDateOfBirth()));
                user.setPassword(passwordEncoder.encode(signup.getPassword()));
                user.setRoles(signup.getRoles()
                        .stream()
                        .map(role -> roleService.findByName(mapToRoleName(role))
                                .orElseThrow(() -> new RuntimeException("Role not found in the database.")))
                        .collect(Collectors.toSet()));

                User savedUser = userRepository.save(user);

                Staff staff = Staff.builder()
                        .user(savedUser)
                        .staffRole(signup.getStaffRole())
                        .dateOfJoining(new SimpleDateFormat("yyyy-MM-dd").parse(signup.getDateOfJoining()))
                        .salary(signup.getSalary())
                        .build();

                staffRepository.save(staff);

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            return Mono.just(user);
        });
    }


    public boolean existsByUsername(String username) {
        return userRepository.existsByUserName(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private RoleName mapToRoleName(String roleName) {
        return switch (roleName) {
            case "STAFF", "staff", "Staff" -> RoleName.STAFF;
            case "USER", "user", "User" -> RoleName.USER;
            default -> null;
        };
    }
}
