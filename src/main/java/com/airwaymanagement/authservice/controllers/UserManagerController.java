package com.airwaymanagement.authservice.controllers;

import com.airwaymanagement.authservice.model.dtos.requests.ChangePasswordRequest;
import com.airwaymanagement.authservice.model.dtos.responses.ResponseMessage;
import com.airwaymanagement.authservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserManagerController {

    private final UserService userService;

    public UserManagerController(UserService userService){
        this.userService = userService;
    }


    @PutMapping("/change-password")
    @Operation(summary = "Change Password",
            description = "Changes Password for authenticated users")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ResponseMessage> changePassword(@RequestBody ChangePasswordRequest request){
        userService.changePassword(request);
        return ResponseEntity.ok(new ResponseMessage("Password Changed Successfully"));
    }
}
