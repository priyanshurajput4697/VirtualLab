
package com.virtuallab.backend.security;

import com.virtuallab.backend.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey key = Keys.hmacShaKeyFor(
        "VirtualLabSecretKeyForJWTAuthentication2026".getBytes(
            StandardCharsets.UTF_8));

    public String generateToken(User user) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
            .subject(user.getEmail())
            .claim("role", user.getRole().name())
            .issuedAt(new Date(now))
            .expiration(new Date(now + 86400000))
            .signWith(key)
            .compact();
    }

    public String extractRole(String token) {
        Object role = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .get("role");
        if (role == null) {
            throw new IllegalArgumentException("Token does not contain a role");
        }
        return role.toString();
    }

    public String extractEmail(String token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }
}
