package com.stegvis_api.stegvis_api.auth;

import com.stegvis_api.stegvis_api.auth.dto.RefreshTokenResponse;
import com.stegvis_api.stegvis_api.auth.dto.UserLoginDTO;
import com.stegvis_api.stegvis_api.auth.dto.UserLoginResponse;
import com.stegvis_api.stegvis_api.auth.dto.UserRegistrationDTO;
import com.stegvis_api.stegvis_api.exception.type.AuthenticationException;
import com.stegvis_api.stegvis_api.exception.type.UserAlreadyExistsException;
import com.stegvis_api.stegvis_api.user.model.User;
import com.stegvis_api.stegvis_api.user.service.UserService;
import com.stegvis_api.stegvis_api.config.security.jwt.JwtTokenService;
import com.stegvis_api.stegvis_api.config.security.jwt.JwtRefreshTokenService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final JwtRefreshTokenService jwtRefreshTokenService;

    public AuthService(UserService userService,
            PasswordEncoder passwordEncoder,
            JwtTokenService jwtTokenService,
            JwtRefreshTokenService jwtRefreshTokenService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
        this.jwtRefreshTokenService = jwtRefreshTokenService;
    }

    public User register(UserRegistrationDTO dto) {
        if (userService.getUserByEmail(dto.getEmail()) != null) {
            throw new UserAlreadyExistsException("E-posten är redan kopplad till ett konto");
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        return userService.saveUser(user);
    }

    public UserLoginResponse login(UserLoginDTO dto, HttpServletResponse response) {
        User user = userService.getUserByEmail(dto.getEmail());
        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Ogiltlig e-post eller lösenord");
        }

        String accessToken = jwtTokenService.generateToken(user.getId());
        String refreshToken = jwtRefreshTokenService.generateToken(user.getId());

        jwtTokenService.setJwtCookie(response, accessToken);
        jwtRefreshTokenService.setRefreshCookie(response, refreshToken);

        return new UserLoginResponse(user.getId(), user.getEmail(), user.isHasCompletedOnboarding());
    }

    public RefreshTokenResponse refreshToken(String refreshToken, HttpServletResponse response) {
        if (refreshToken == null) {
            throw new AuthenticationException("Refresh token is missing");
        }

        String userId;
        try {
            userId = jwtRefreshTokenService.validateAndGetUserId(refreshToken);
        } catch (AuthenticationException e) {
            jwtTokenService.clearJwtCookie(response);
            jwtRefreshTokenService.clearRefreshCookie(response);
            throw new AuthenticationException("Refresh token is invalid or expired");
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            jwtTokenService.clearJwtCookie(response);
            jwtRefreshTokenService.clearRefreshCookie(response);
            throw new AuthenticationException("Invalid refresh token");
        }

        String newAccessToken = jwtTokenService.generateToken(userId);
        jwtTokenService.setJwtCookie(response, newAccessToken);

        String newRefreshToken = jwtRefreshTokenService.generateToken(userId);
        jwtRefreshTokenService.setRefreshCookie(response, newRefreshToken);

        return new RefreshTokenResponse(user.getId(), user.getEmail());
    }

    public void logout(HttpServletResponse response) {
        jwtTokenService.clearJwtCookie(response);
        jwtRefreshTokenService.clearRefreshCookie(response);
    }
}