package com.airwaymanagement.authservice.security.usersecurity;

import com.airwaymanagement.authservice.model.entities.User;
import com.airwaymanagement.authservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.beans.Transient;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetail loadUserByUsername(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new RuntimeException("Email or Username does not exist, please try again: " + userName));

        return UserDetail.build(user);
    }

    @Transactional
    public UserDetail loadUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email or Username does not exist, please try again: " + email));

        return UserDetail.build(user);
    }
}
