package com.airwaymanagement.authservice.controllers;

import com.airwaymanagement.authservice.model.dtos.requests.Login;
import com.airwaymanagement.authservice.model.dtos.requests.Signup;
import com.airwaymanagement.authservice.model.dtos.requests.StaffSignup;
import com.airwaymanagement.authservice.model.dtos.responses.JWTResponseMessage;
import com.airwaymanagement.authservice.model.dtos.responses.ResponseMessage;
import com.airwaymanagement.authservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class UserAuthenticationController {
    private final UserService userService;

    public UserAuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> register(@RequestBody Signup signup) {
        try {
            userService.register(signup);
            return new ResponseEntity<>(
                    new ResponseMessage("Create user: " + signup.getUserName() + " successfully."),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ResponseMessage("Error occurred while creating the account."),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PostMapping("/register/staff")
    public ResponseEntity<ResponseMessage> registerStaff(@RequestBody StaffSignup staffSignup) {
        try {
            userService.registerStaff(staffSignup);
            return new ResponseEntity<>(
                    new ResponseMessage("Create user: " + staffSignup.getUserName() + " successfully."),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ResponseMessage("Error occurred while creating the account."),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JWTResponseMessage> login(@RequestBody Login login) {
        try {
            return new ResponseEntity<>(userService.login(login), HttpStatus.OK);
        } catch (Exception e) {
            JWTResponseMessage errorJwtResponseMessage = new JWTResponseMessage(
                    null,
                    null,
                    "Error: Retry Again"
            );
            return new ResponseEntity<>(errorJwtResponseMessage, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated() and hasAuthority('USER')")
    public ResponseEntity<String> logout() {
        try {
             userService.logout();
             return new ResponseEntity<>("Logout Successful", HttpStatus.OK);
        } catch (Exception e) {
             return new ResponseEntity<>("Logout failed.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/hello")
    @Operation(summary = "Get a hello message",
            description = "Returns a hello message for authenticated users")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Hello, authenticated user!");
    }
}
