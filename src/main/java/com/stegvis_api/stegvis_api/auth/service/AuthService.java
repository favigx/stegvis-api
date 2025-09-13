package com.stegvis_api.stegvis_api.auth.service;

import com.stegvis_api.stegvis_api.auth.dto.UserLoginDTO;
import com.stegvis_api.stegvis_api.auth.dto.UserRegistrationDTO;
import com.stegvis_api.stegvis_api.exception.type.AuthenticationException;
import com.stegvis_api.stegvis_api.exception.type.ResourceAlreadyExistsException;
import com.stegvis_api.stegvis_api.exception.type.ResourceNotFoundException;
import com.stegvis_api.stegvis_api.repository.UserRepository;
import com.stegvis_api.stegvis_api.user.model.User;
import com.stegvis_api.stegvis_api.config.security.jwt.JwtTokenService;
import com.stegvis_api.stegvis_api.config.security.jwt.JwtRefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;

@Slf4j
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final JwtRefreshTokenService jwtRefreshTokenService;

    public AuthService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenService jwtTokenService,
            JwtRefreshTokenService jwtRefreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
        this.jwtRefreshTokenService = jwtRefreshTokenService;
    }

    public User register(UserRegistrationDTO dto) {
        userRepository.findByEmail(dto.getEmail())
                .ifPresent(user -> {
                    log.warn("Registration attempt failed: email already exists");
                    throw new ResourceAlreadyExistsException(
                            "E-posten är redan kopplad till ett konto");
                });

        User user = User.builder()
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: id={}", savedUser.getId());
        return savedUser;
    }

    public User login(UserLoginDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed: invalid credentials");
                    return new AuthenticationException("Ogiltlig e-post eller lösenord");
                });

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            log.warn("Login failed: invalid credentials for user id={}", user.getId());
            throw new AuthenticationException("Ogiltlig e-post eller lösenord");
        }

        log.info("User logged in successfully: id={}", user.getId());
        return user;
    }

    public User refreshUserFromToken(String refreshToken) {
        if (refreshToken == null) {
            log.warn("Refresh token missing");
            throw new AuthenticationException("Refresh token saknas");
        }

        String userId;
        try {
            userId = jwtRefreshTokenService.validateAndGetUserId(refreshToken);
        } catch (AuthenticationException e) {
            log.warn("Invalid or expired refresh token");
            throw new AuthenticationException("Refresh token är ogiltig eller har gått ut");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found for id={}", userId);
                    return new ResourceNotFoundException("Användaren med id " + userId + " hittades inte");
                });

        log.info("Refresh token validated for user id={}", userId);
        return user;
    }

    public void setTokens(User user, HttpServletResponse response) {
        String newAccessToken = jwtTokenService.generateToken(user.getId());
        String newRefreshToken = jwtRefreshTokenService.generateToken(user.getId());

        jwtTokenService.setJwtCookie(response, newAccessToken);
        jwtRefreshTokenService.setRefreshCookie(response, newRefreshToken);

        log.debug("JWT and refresh token set for user id={}", user.getId());
    }

    public void clearTokens(HttpServletResponse response) {
        jwtTokenService.clearJwtCookie(response);
        jwtRefreshTokenService.clearRefreshCookie(response);
        log.debug("Cleared JWT and refresh tokens");
    }
}