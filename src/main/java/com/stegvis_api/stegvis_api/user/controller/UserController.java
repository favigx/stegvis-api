package com.stegvis_api.stegvis_api.user.controller;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stegvis_api.stegvis_api.config.security.UserPrincipal;
import com.stegvis_api.stegvis_api.user.dto.AddUserPreferenceOnboardingDTO;
import com.stegvis_api.stegvis_api.user.dto.DeleteUserResponse;
import com.stegvis_api.stegvis_api.user.dto.DeleteUserResult;
import com.stegvis_api.stegvis_api.user.dto.GetUserPreferenceResponse;
import com.stegvis_api.stegvis_api.user.dto.UserPreferenceResponse;
import com.stegvis_api.stegvis_api.user.model.User;
import com.stegvis_api.stegvis_api.user.model.UserPreference;
import com.stegvis_api.stegvis_api.user.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/preferences")
    public ResponseEntity<UserPreferenceResponse> updateUserPreferences(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody AddUserPreferenceOnboardingDTO preferenceDTO) {

        UserPreference updatedPreference = userService.setUserPreferences(userPrincipal.getId(), preferenceDTO);

        UserPreferenceResponse response = UserPreferenceResponse.builder()
                .userId(userPrincipal.getId())
                .userPreference(updatedPreference)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/preferences")
    public ResponseEntity<GetUserPreferenceResponse> getUserPreferences(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        UserPreference userPreference = userService.getUserPreferences(userPrincipal.getId());

        GetUserPreferenceResponse response = GetUserPreferenceResponse.builder()
                .userId(userPrincipal.getId())
                .userPreference(userPreference)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<DeleteUserResponse> deleteUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        DeleteUserResult result = userService.deleteUser(userPrincipal.getId());

        DeleteUserResponse response = DeleteUserResponse.builder()
                .id(userPrincipal.getId())
                .deletedNotes(result.deletedNotes())
                .deletedTodos(result.deletedTodos())
                .deletedTasks(result.deletedTasks())
                .deletedAt(Instant.now().toString())
                .status("SUCCESS")
                .message("Användaren och alla relaterade resurser har raderats.")
                .build();

        return ResponseEntity.ok(response);
    }
}