package com.airwaymanagement.authservice.model.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JWTResponseMessage {

    private String accessToken;
    private String refreshToken;
    private String info;

}
