package com.airwaymanagement.authservice.controllers;

import com.airwaymanagement.authservice.model.dtos.requests.Signup;
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

    @ApiResponses({
            @ApiResponse(code = 200, message = "User created successfully", response = ResponseMessage.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ResponseMessage.class)
    })
    @PostMapping({"/signup", "/register"})
    public Mono<ResponseMessage> register(@RequestBody Signup signup) {
        return userService.register(signup)
                .map(user -> new ResponseMessage("Create user: " + signup.getUserName() + " successfully."))
                .onErrorResume(error -> Mono.just(new ResponseMessage("Error occurred while creating the account.")));
    }
}
