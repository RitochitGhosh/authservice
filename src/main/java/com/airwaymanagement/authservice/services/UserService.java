package com.airwaymanagement.authservice.services;

import com.airwaymanagement.authservice.model.dtos.requests.ChangePasswordRequest;
import com.airwaymanagement.authservice.model.dtos.requests.Login;
import com.airwaymanagement.authservice.model.dtos.requests.Signup;
import com.airwaymanagement.authservice.model.dtos.requests.StaffSignup;
import com.airwaymanagement.authservice.model.dtos.responses.JWTResponseMessage;
import com.airwaymanagement.authservice.model.dtos.responses.ResponseMessage;
import com.airwaymanagement.authservice.model.entities.Staff;
import com.airwaymanagement.authservice.model.entities.User;
import reactor.core.publisher.Mono;

public interface UserService {
    User register(Signup signup);
    Staff registerStaff(StaffSignup staffSignup);
    JWTResponseMessage login(Login login);
    ResponseMessage changePassword(ChangePasswordRequest request);
    void logout();

    // TODO : Include Other 4 Functions
}
