package com.stegvis_api.stegvis_api.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stegvis_api.stegvis_api.config.security.UserPrincipal;
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