package com.airwaymanagement.authservice.model.dtos.requests;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Setter
@Getter
@RequiredArgsConstructor
public class Signup {
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private String dateOfBirth;
    private String govId;
    private String passportId;
    private String nationality;
    private String address;
    private String gender;
    private Set<String> roles;
}
