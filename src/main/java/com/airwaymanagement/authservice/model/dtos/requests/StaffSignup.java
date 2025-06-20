package com.airwaymanagement.authservice.model.dtos.requests;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class StaffSignup extends Signup{
    private String staffRole;
    private String dateOfJoining;
    private String salary;
}
