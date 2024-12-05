package com.epam.learn.trainerworkloadservice.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtTokenValidator {
    private final String secret = "gym-management-system-application";
    private final SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());

    public void validateToken(String token) {
        Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
