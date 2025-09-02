package com.stegvis_api.stegvis_api.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stegvis_api.stegvis_api.user.dto.GetUserPreferenceResponse;
import com.stegvis_api.stegvis_api.user.dto.UserPreferenceResponse;
import com.stegvis_api.stegvis_api.user.model.User;
import com.stegvis_api.stegvis_api.user.model.UserPreference;
import com.stegvis_api.stegvis_api.user.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("#userId == principal.id")
    @PutMapping("/{id}/preferences")
    public ResponseEntity<UserPreferenceResponse> updateUserPreferences(
            @PathVariable("id") String userId,
            @RequestBody UserPreference userPreference) {

        User updatedUser = userService.setUserPreferences(userId, userPreference);

        UserPreferenceResponse response = UserPreferenceResponse.builder()
                .userId(updatedUser.getId())
                .userPreference(updatedUser.getUserPreference())
                .build();

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("#userId == principal.id")
    @GetMapping("/{id}/preferences")
    public ResponseEntity<GetUserPreferenceResponse> getUserPreferences(
            @PathVariable("id") String userId) {

        UserPreference userPreference = userService.getUserPreferences(userId);

        GetUserPreferenceResponse response = GetUserPreferenceResponse.builder()
                .userId(userId)
                .userPreference(userPreference)
                .build();

        return ResponseEntity.ok(response);
    }
}