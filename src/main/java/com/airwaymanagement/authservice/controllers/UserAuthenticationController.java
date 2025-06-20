package com.airwaymanagement.authservice.controllers;

import com.airwaymanagement.authservice.model.dtos.requests.Signup;
import com.airwaymanagement.authservice.model.dtos.requests.StaffSignup;
import com.airwaymanagement.authservice.model.dtos.responses.ResponseMessage;
import com.airwaymanagement.authservice.services.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class UserAuthenticationController {
    private final UserService userService;

    public UserAuthenticationController(UserService userService){
        this.userService = userService;
    }

    @PostMapping({"/signup", "/register"})
    public Mono<ResponseMessage> register(@RequestBody Signup signup) {
        return userService.register(signup)
                .map(user -> new ResponseMessage("Create user: " + signup.getUserName() + " successfully."))
                .onErrorResume(error -> Mono.just(new ResponseMessage("Error occurred while creating the account.")));
    }

    @PostMapping({"/signup/staff", "/register/staff"})
    public Mono<ResponseMessage> registerStaff(@RequestBody StaffSignup staffSignup){
        return userService.registerStaff(staffSignup)
                .map(user -> new ResponseMessage("Create staff: " + staffSignup.getUserName() + " successfully."))
                .onErrorResume(error -> Mono.just(new ResponseMessage("Error occurred while creating the account.")));
    }
}
