package com.stegvis_api.stegvis_api.config.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.exception.type.AuthenticationException;

import java.security.Key;
import java.util.Date;

@Service
public class JwtRefreshTokenService {

    private final Key refreshKey;
    private final long refreshExpirationMs;

    public JwtRefreshTokenService(@Value("${stegvis.refresh-token.jwt.secret}") String refreshSecret,
            @Value("${stegvis.refresh-token.jwt.expiration-ms}") long refreshExpirationMs) {
        this.refreshKey = Keys.hmacShaKeyFor(refreshSecret.getBytes());
        this.refreshExpirationMs = refreshExpirationMs;
    }

    public String generateToken(String userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshExpirationMs);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuer("stegvis-api")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(refreshKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public void setRefreshCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from("refresh-jwt", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshExpirationMs / 1000)
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public void clearRefreshCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refresh-jwt", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    public String validateAndGetUserId(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(refreshKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            throw new AuthenticationException("Refresh token invalid or expired");
        }
    }
}