package com.stegvis_api.stegvis_api.auth.service;

import com.stegvis_api.stegvis_api.auth.dto.RefreshTokenResponse;
import com.stegvis_api.stegvis_api.auth.dto.UserLoginDTO;
import com.stegvis_api.stegvis_api.auth.dto.UserLoginResponse;
import com.stegvis_api.stegvis_api.auth.dto.UserRegistrationDTO;
import com.stegvis_api.stegvis_api.auth.dto.UserRegistrationResponse;
import com.stegvis_api.stegvis_api.exception.type.AuthenticationException;
import com.stegvis_api.stegvis_api.exception.type.ResourceAlreadyExistsException;
import com.stegvis_api.stegvis_api.exception.type.ResourceNotFoundException;
import com.stegvis_api.stegvis_api.mappers.AuthMapper;
import com.stegvis_api.stegvis_api.repository.UserRepository;
import com.stegvis_api.stegvis_api.user.model.User;
import com.stegvis_api.stegvis_api.config.security.jwt.JwtTokenService;
import com.stegvis_api.stegvis_api.config.security.jwt.JwtRefreshTokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final JwtRefreshTokenService jwtRefreshTokenService;
    private final AuthMapper authMapper;

    @Transactional
    public UserRegistrationResponse register(UserRegistrationDTO dto) {
        userRepository.findByEmail(dto.email())
                .ifPresent(user -> {
                    log.warn("Registration attempt failed: email already exists");
                    throw new ResourceAlreadyExistsException(
                            "E-posten är redan kopplad till ett konto");
                });

        User user = authMapper.toUserRegistrationDTO(dto);

        user.setPassword(passwordEncoder.encode(dto.password()));

        User savedUser = userRepository.save(user);

        log.info("User registered successfully: id={}", savedUser.getId());
        return authMapper.toUserRegistrationResponse(savedUser);
    }

    @Transactional
    public UserLoginResponse login(UserLoginDTO dto, HttpServletResponse response) {
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> {
                    log.warn("Login failed: invalid credentials for email={}", dto.email());
                    return new AuthenticationException("Ogiltig e-post eller lösenord");
                });

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            log.warn("Login failed: invalid credentials for user id={}", user.getId());
            throw new AuthenticationException("Ogiltig e-post eller lösenord");
        }

        setTokens(user, response);

        log.info("User logged in successfully: id={}", user.getId());

        return authMapper.toUserLoginResponse(user);
    }

    @Transactional
    public RefreshTokenResponse refreshUserFromToken(String refreshToken, HttpServletResponse response) {
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

        setTokens(user, response);

        log.info("Refresh token validated for user id={}", userId);

        return authMapper.toRefreshTokenResponse(user);
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