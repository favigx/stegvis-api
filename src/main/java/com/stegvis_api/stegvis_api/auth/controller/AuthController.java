package com.stegvis_api.stegvis_api.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.stegvis_api.stegvis_api.auth.dto.RefreshTokenResponse;
import com.stegvis_api.stegvis_api.auth.dto.UserLoginDTO;
import com.stegvis_api.stegvis_api.auth.dto.UserLoginResponse;
import com.stegvis_api.stegvis_api.auth.dto.UserRegistrationDTO;
import com.stegvis_api.stegvis_api.auth.dto.UserRegistrationResponse;
import com.stegvis_api.stegvis_api.auth.service.AuthService;
import com.stegvis_api.stegvis_api.user.model.User;

import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> registerUser(@RequestBody UserRegistrationDTO dto) {
        User user = authService.register(dto);

        UserRegistrationResponse response = UserRegistrationResponse.builder()
                .id(user.getId())
                .fName(user.getFName())
                .lName(user.getLName())
                .email(user.getEmail())
                .build();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody UserLoginDTO dto, HttpServletResponse response) {
        User user = authService.login(dto);

        authService.setTokens(user, response);

        UserLoginResponse responseDto = UserLoginResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .hasCompletedOnboarding(user.isHasCompletedOnboarding())
                .build();

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(
            @CookieValue(value = "refresh-jwt", required = false) String refreshToken,
            HttpServletResponse response) {

        User user = authService.refreshUserFromToken(refreshToken);

        authService.setTokens(user, response);

        RefreshTokenResponse responseDto = RefreshTokenResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        authService.clearTokens(response);
        return ResponseEntity.noContent().build();
    }
}