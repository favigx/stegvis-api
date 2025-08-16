package com.stegvis_api.stegvis_api.user.controller;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.stegvis_api.stegvis_api.config.security.UserPrincipal;
import com.stegvis_api.stegvis_api.user.dto.UserLoginDTO;
import com.stegvis_api.stegvis_api.user.dto.UserLoginResponse;
import com.stegvis_api.stegvis_api.user.dto.UserPreferenceResponse;
import com.stegvis_api.stegvis_api.user.dto.UserRegistrationDTO;
import com.stegvis_api.stegvis_api.user.dto.UserRegistrationResponse;
import com.stegvis_api.stegvis_api.user.model.User;
import com.stegvis_api.stegvis_api.user.model.UserPreference;
import com.stegvis_api.stegvis_api.user.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> registerUser(
            @Valid @RequestBody UserRegistrationDTO userRegistrationDTO) {
        User user = userService.addUser(userRegistrationDTO);

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
    public ResponseEntity<UserLoginResponse> loginUser(
            @Valid @RequestBody UserLoginDTO loginDTO,
            HttpServletResponse response) {

        UserLoginResponse loginResponse = userService.loginUser(loginDTO, response);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/preferences")
    public ResponseEntity<UserPreferenceResponse> updateUserPreferences(
            @PathVariable("id") String userId,
            @RequestBody UserPreference userPreference,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (!userId.equals(userPrincipal.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User updatedUser = userService.setUserPreferences(userId, userPreference);

        UserPreferenceResponse response = UserPreferenceResponse.builder()
                .userId(updatedUser.getId())
                .userPreference(updatedUser.getUserPreference())
                .build();

        return ResponseEntity.ok(response);
    }
}
