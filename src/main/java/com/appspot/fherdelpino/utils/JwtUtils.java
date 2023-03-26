package com.appspot.fherdelpino.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    public String generateJwtToken(Authentication authentication) {
        Instant now = Instant.now();
        if (authentication.getPrincipal() instanceof User user) {
            return Jwts.builder()
                    .setSubject((user.getUsername()))
                    .setIssuedAt(Date.from(now))
                    .setExpiration(Date.from(now.plus(5, ChronoUnit.MINUTES)))
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();
        } else {
            throw new RuntimeException("Principal is not an instance of Spring User");
        }

    }

    public String getUserName(String jwtToken) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwtToken)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (JwtException e) {
            log.error("Invalid JWT: {}", e.getMessage());
        }

        return false;
    }
}
