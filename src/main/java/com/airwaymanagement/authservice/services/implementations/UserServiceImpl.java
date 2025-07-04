package com.airwaymanagement.authservice.services.implementations;

import com.airwaymanagement.authservice.model.dtos.requests.ChangePasswordRequest;
import com.airwaymanagement.authservice.model.dtos.requests.Login;
import com.airwaymanagement.authservice.model.dtos.requests.Signup;
import com.airwaymanagement.authservice.model.dtos.requests.StaffSignup;
import com.airwaymanagement.authservice.model.dtos.responses.JWTResponseMessage;
import com.airwaymanagement.authservice.model.dtos.responses.ResponseMessage;
import com.airwaymanagement.authservice.model.entities.RoleName;
import com.airwaymanagement.authservice.model.entities.Staff;
import com.airwaymanagement.authservice.model.entities.User;
import com.airwaymanagement.authservice.repository.StaffRepository;
import com.airwaymanagement.authservice.repository.UserRepository;
import com.airwaymanagement.authservice.security.jsonwebtokens.TokenProvider;
import com.airwaymanagement.authservice.security.usersecurity.UserDetail;
import com.airwaymanagement.authservice.security.usersecurity.UserDetailService;
import com.airwaymanagement.authservice.services.RoleService;
import com.airwaymanagement.authservice.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.Optional;
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

    @Override
    public User register(Signup signup) {

        if (existsByUsername(signup.getUserName())) {
            throw new RuntimeException("The username " + signup.getUserName() + " is existed, please try again.");
        } else if (existsByEmail(signup.getEmail())) {
            throw new RuntimeException("The email " + signup.getEmail() + " is existed, please try again.");
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

            return userRepository.save(user);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Staff registerStaff(StaffSignup signup) {
        if (existsByUsername(signup.getUserName())) {
            throw new RuntimeException("The username " + signup.getUserName() + " is existed, please try again.");
        } else if (existsByEmail(signup.getEmail())) {
            throw new RuntimeException("The email " + signup.getEmail() + " is existed, please try again.");
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

            return staffRepository.save(staff);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public JWTResponseMessage login(Login login) {
        String username = login.getUsername();

        UserDetails userDetails;
        userDetails = userDetailService.loadUserByUsername(username);

        if (userDetails == null) {
            throw new RuntimeException("User not found");
        }

        if (!passwordEncoder.matches(login.getPassword(), userDetails.getPassword())) {
            throw new RuntimeException("Incorrect Password");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, login.getPassword(), userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.generateToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        UserDetail userDetail = (UserDetail) userDetails;

        return JWTResponseMessage.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .info("User Successfully Logged in").build();

    }

    @Override
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = getCurrentToken();

        String username = tokenProvider.getUserNameFromToken(token);
        UserDetails userDetails = userDetailService.loadUserByUsername(username);

        if (authentication != null && authentication.isAuthenticated()) {
            tokenProvider.reduceTokenExpiration(token);
        }

        SecurityContextHolder.getContext().setAuthentication(null);

        SecurityContextHolder.clearContext();
    }

    @Override
    public ResponseMessage changePassword(ChangePasswordRequest request) {

        try {
            UserDetails userDetails = getCurrentUserDetails();
            String username = userDetails.getUsername();

            User existingUser = findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User Not Found with username: " + username));

            if (passwordEncoder.matches(request.getOldPassword(), userDetails.getPassword())) {
                if (Objects.equals(request.getNewPassword(), request.getConfirmNewPassword())) {
                    existingUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
                    userRepository.save(existingUser);
                } else {
                    return new ResponseMessage("Password and Confirm Password is not same");
                }
            } else {
                return new ResponseMessage("Incorrect password");
            }
            return new ResponseMessage("Password Changed Successfully");
        } catch (Exception e) {
            return new ResponseMessage("Transaction silently rolled back");
        }

    }

    public String getCurrentToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object credentials = authentication.getCredentials();

            if (credentials instanceof String) {
                return (String) credentials;
            }
        }

        return null;
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUserName(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<User> findByUsername(String userName) {
        return Optional.ofNullable(userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("User not found with userName: " + userName)));
    }

    private UserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            return (UserDetails) authentication.getPrincipal();
        } else {
            throw new RuntimeException("User not authenticated.");
        }
    }

    private RoleName mapToRoleName(String roleName) {
        return switch (roleName) {
            case "STAFF", "staff", "Staff" -> RoleName.STAFF;
            case "USER", "user", "User" -> RoleName.USER;
            default -> null;
        };
    }
}
