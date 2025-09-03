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

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;

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
                    throw new ResourceAlreadyExistsException(
                            "E-posten är redan kopplad till ett konto");
                });

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        return userRepository.save(user);
    }

    public User login(UserLoginDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new AuthenticationException("Ogiltlig e-post eller lösenord"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Ogiltlig e-post eller lösenord");
        }
        return user;
    }

    public User refreshUserFromToken(String refreshToken) {
        if (refreshToken == null) {
            throw new AuthenticationException("Refresh token saknas");
        }

        String userId;
        try {
            userId = jwtRefreshTokenService.validateAndGetUserId(refreshToken);
        } catch (AuthenticationException e) {
            throw new AuthenticationException("Refresh token är ogiltig eller har gått ut");
        }

        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Användaren med id " + userId + " hittades inte"));
    }

    public void setTokens(User user, HttpServletResponse response) {
        String newAccessToken = jwtTokenService.generateToken(user.getId());
        String newRefreshToken = jwtRefreshTokenService.generateToken(user.getId());

        jwtTokenService.setJwtCookie(response, newAccessToken);
        jwtRefreshTokenService.setRefreshCookie(response, newRefreshToken);
    }

    public void clearTokens(HttpServletResponse response) {
        jwtTokenService.clearJwtCookie(response);
        jwtRefreshTokenService.clearRefreshCookie(response);
    }
}
