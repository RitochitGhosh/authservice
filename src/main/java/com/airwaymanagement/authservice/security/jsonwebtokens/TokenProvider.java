package com.airwaymanagement.authservice.security.jsonwebtokens;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    @Value("${jwt.secret}")
    private String jwtSecret; // Secret Key

    @Value("${jwt.expiration}")
    private int jwtExpiration; // Expiration in Seconds

    @Value("${jwt.refreshExpiration}")
    private int jwtRefreshExpiration; // RefreshToken Expiration in Seconds

    // Secret Key Generator
    private SecretKey getJWTSecretKey(String jwtSecret){
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(Authentication authentication) {

        // Roles are extracted from authentication and returns with joined with ','
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // JSON Web Token Building Stage
        return Jwts.builder()
                .subject(authentication.getName())
                .issuer("application")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration * 1000L)) // 1 day
                .claim("authorities", authorities)
                .signWith(getJWTSecretKey(jwtSecret))
                .compact();
    }

    public String generateRefreshToken(Authentication authentication){

        // Creates RefreshToken
        return Jwts.builder()
                .subject(authentication.getName())
                .issuer("application")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtRefreshExpiration * 1000L)) // 2 Days
                .signWith(getJWTSecretKey(jwtSecret))
                .compact();
    }

    public String reduceTokenExpiration(String token) {

        // Decode the token to extract its claims
        Claims claims = Jwts.parser()
                .verifyWith(getJWTSecretKey(jwtSecret))
                .build()
                .parseUnsecuredClaims(token)
                .getPayload();

        // Build a new token with the updated expiration time
        return Jwts.builder()
                .claims(claims)
                .expiration(new Date(0))
                .signWith(getJWTSecretKey(jwtSecret))
                .compact();
    }

    public Boolean validateToken(String token){
        try {
            Jwts.parser()
                    .verifyWith(getJWTSecretKey(jwtSecret))
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (Exception e){
            logger.error("Token isn't Validated -> Error: ", e);
        }

        return false;
    }

    public String getUserNameFromToken(String token){
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getJWTSecretKey(jwtSecret))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getSubject();
        } catch (Exception e) {
            logger.error("No Username Found -> Error: ", e);
        }
        return null;
    }
}
