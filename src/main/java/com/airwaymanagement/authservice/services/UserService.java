package com.airwaymanagement.authservice.services;

import com.airwaymanagement.authservice.model.dtos.requests.Signup;
import com.airwaymanagement.authservice.model.dtos.requests.StaffSignup;
import com.airwaymanagement.authservice.model.entities.User;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> register(Signup signup);
    Mono<User> registerStaff(StaffSignup staffSignup);

    // TODO : Include Other 6 Functions
}
