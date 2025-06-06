package com.airwaymanagement.authservice.services;

import com.airwaymanagement.authservice.model.dtos.requests.Signup;
import com.airwaymanagement.authservice.model.entities.User;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> register(Signup signup);
    // TODO : Include Other 6 Functions
}
