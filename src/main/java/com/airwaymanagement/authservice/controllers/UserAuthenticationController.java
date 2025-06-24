package com.airwaymanagement.authservice.controllers;

import com.airwaymanagement.authservice.model.dtos.requests.ChangePasswordRequest;
import com.airwaymanagement.authservice.model.dtos.requests.Login;
import com.airwaymanagement.authservice.model.dtos.requests.Signup;
import com.airwaymanagement.authservice.model.dtos.requests.StaffSignup;
import com.airwaymanagement.authservice.model.dtos.responses.JWTResponseMessage;
import com.airwaymanagement.authservice.model.dtos.responses.ResponseMessage;
import com.airwaymanagement.authservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class UserAuthenticationController {
    private final UserService userService;

    public UserAuthenticationController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public Mono<ResponseMessage> register(@RequestBody Signup signup) {
        return userService.register(signup)
                .map(user -> new ResponseMessage("Create user: " + signup.getUserName() + " successfully."))
                .onErrorResume(error -> Mono.just(new ResponseMessage("Error occurred while creating the account.")));
    }

    @PostMapping("/register/staff")
    public Mono<ResponseMessage> registerStaff(@RequestBody StaffSignup staffSignup){
        return userService.registerStaff(staffSignup)
                .map(user -> new ResponseMessage("Create staff: " + staffSignup.getUserName() + " successfully."))
                .onErrorResume(error -> Mono.just(new ResponseMessage("Error occurred while creating the account.")));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<JWTResponseMessage>> login (@RequestBody Login login){
        return userService.login(login)
                .map(ResponseEntity::ok)
                .onErrorResume(error -> {
                    JWTResponseMessage errorjwtResponseMessage = new JWTResponseMessage(
                            null,
                            null,
                            "Error: Retry Again"
                    );
                    return Mono.just(new ResponseEntity<>(errorjwtResponseMessage, HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    @GetMapping("/hello")
    @Operation(summary = "Get a hello message",
            description = "Returns a hello message for authenticated users")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello, authenticated user!");
    }
}
