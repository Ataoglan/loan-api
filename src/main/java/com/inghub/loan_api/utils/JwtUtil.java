package com.inghub.loan_api.utils;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.key}")
    private String SECRET_KEY;

    public String generateToken(String tckn, String role, Long userId) {
        return Jwts.builder()
                .setSubject(tckn)
                .claim("role", role)
                .claim("userId", userId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
                .compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractTckn(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public boolean isTokenValid(String token, String tckn) {
        final String extractedTckn = extractTckn(token);
        return (extractedTckn.equals(tckn) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public String extractTcknFromToken() {
        String authorizationHeader = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return extractTckn(token);
        }
        throw new IllegalStateException("Invalid token or no token provided");
    }

    public String extractRoleFromToken() {
        String authorizationHeader = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return extractRole(token);
        }
        throw new IllegalStateException("Invalid token or no token provided");
    }

    public Long extractUserIdFromToken() {
        String authorizationHeader = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return extractUserId(token);
        }
        throw new IllegalStateException("Invalid token or no token provided");
    }

    private Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId", Long.class);
    }
}

