package com.airwaymanagement.authservice.jwt;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.stream.Collectors;

public class TokenProvider {
    SecretKey key = Keys.hmacShaKeyFor(JwtConstants.JWT_SECRET.getBytes());

    public String generateToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        String jwt = Jwts.builder()
                .setIssuer("application")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .claim("email", authentication.getName())
                .claim("authorities", authorities)
                .signWith(key)
                .compact();

        return jwt;
    }
    public String getEmailFromToken (String jwt) {
        jwt = jwt.split(" ")[1];
        Claims claim = Jwts.parser().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

        String email = String.valueOf(claim.get("email"));
        return email;
    }
}
