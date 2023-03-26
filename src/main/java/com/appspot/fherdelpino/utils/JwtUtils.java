package com.appspot.fherdelpino.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class JwtUtils {

    private static final String JWT_SECRET = "t35t";

    public static String generateJwtToken(Authentication authentication) {
        Instant now = Instant.now();
        if (authentication.getPrincipal() instanceof User user) {
            return Jwts.builder()
                    .setSubject((user.getUsername()))
                    .setIssuedAt(Date.from(now))
                    .setExpiration(Date.from(now.plus(5, ChronoUnit.MINUTES)))
                    .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                    .compact();
        } else {
            throw new RuntimeException("Principal is not an instance of Spring User");
        }

    }
}
