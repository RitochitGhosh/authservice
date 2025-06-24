package com.airwaymanagement.authservice.services;

import com.airwaymanagement.authservice.model.dtos.requests.ChangePasswordRequest;
import com.airwaymanagement.authservice.model.dtos.requests.Login;
import com.airwaymanagement.authservice.model.dtos.requests.Signup;
import com.airwaymanagement.authservice.model.dtos.requests.StaffSignup;
import com.airwaymanagement.authservice.model.dtos.responses.JWTResponseMessage;
import com.airwaymanagement.authservice.model.entities.User;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> register(Signup signup);
    Mono<User> registerStaff(StaffSignup staffSignup);
    Mono<JWTResponseMessage> login(Login login);
    Mono<String> changePassword(ChangePasswordRequest request);
    Mono<Void> logout();

    // TODO : Include Other 4 Functions
}
