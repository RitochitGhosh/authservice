package com.airwaymanagement.authservice.services.implementations;

import com.airwaymanagement.authservice.repository.UserRepository;
import com.airwaymanagement.authservice.security.jsonwebtokens.TokenProvider;
import com.airwaymanagement.authservice.security.usersecurity.UserDetailService;
import com.airwaymanagement.authservice.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// TODO : Implement User Service (Currently Ongoing)
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final UserDetailService userDetailService;
    private final ModelMapper modelMapper;

}
