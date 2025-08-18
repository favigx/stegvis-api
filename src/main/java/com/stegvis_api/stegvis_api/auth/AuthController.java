package com.stegvis_api.stegvis_api.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.stegvis_api.stegvis_api.auth.dto.UserLoginDTO;
import com.stegvis_api.stegvis_api.auth.dto.UserLoginResponse;
import com.stegvis_api.stegvis_api.auth.dto.UserRegistrationDTO;
import com.stegvis_api.stegvis_api.auth.dto.UserRegistrationResponse;

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

        UserRegistrationResponse response = new UserRegistrationResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/login")
    public UserLoginResponse login(@RequestBody UserLoginDTO dto, HttpServletResponse response) {
        return authService.login(dto, response);
    }

    @PostMapping("/refresh")
    public UserLoginResponse refresh(@CookieValue(value = "refresh-jwt", required = false) String refreshToken,
            HttpServletResponse response) {
        return authService.refreshToken(refreshToken, response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.noContent().build();
    }
}