package com.stegvis_api.stegvis_api.config.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.exception.type.AuthenticationException;

import java.security.Key;
import java.util.Date;

@Slf4j
@Service
public class JwtTokenService {

    private final Key jwtKey;
    private final long jwtExpirationMs;

    public JwtTokenService(@Value("${stegvis.jwt.secret}") String jwtSecret,
            @Value("${stegvis.jwt.expiration-ms}") long jwtExpirationMs) {
        this.jwtKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String generateToken(String userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        String token = Jwts.builder()
                .setSubject(userId)
                .setIssuer("stegvis-api")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(jwtKey, SignatureAlgorithm.HS256)
                .compact();

        log.debug("Generated new JWT access token for userId={}, expiresAt={}", userId, expiryDate);
        return token;
    }

    public void setJwtCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(jwtExpirationMs / 1000)
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        log.debug("Set JWT cookie with expiration={} seconds", jwtExpirationMs / 1000);
    }

    public void clearJwtCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        log.debug("Cleared JWT cookie");
    }

    public String validateAndGetUserId(String token) {
        try {
            String userId = Jwts.parserBuilder()
                    .setSigningKey(jwtKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            log.debug("Validated JWT token for userId={}", userId);
            return userId;
        } catch (Exception e) {
            log.warn("Failed to validate JWT token: {}", e.getMessage());
            throw new AuthenticationException("JWT token invalid or expired");
        }
    }
}