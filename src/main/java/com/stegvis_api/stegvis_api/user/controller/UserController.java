package com.stegvis_api.stegvis_api.user.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stegvis_api.stegvis_api.config.security.UserPrincipal;
import com.stegvis_api.stegvis_api.user.dto.AddGradeGoalDTO;
import com.stegvis_api.stegvis_api.user.dto.AddGradeGoalResponse;
import com.stegvis_api.stegvis_api.user.dto.AddOnboardingPreferencesDTO;
import com.stegvis_api.stegvis_api.user.dto.AddOnboardingPreferencesResponse;
import com.stegvis_api.stegvis_api.user.dto.AddSubjectPreferencesDTO;
import com.stegvis_api.stegvis_api.user.dto.AddSubjectPreferencesResponse;
import com.stegvis_api.stegvis_api.user.dto.AddSubjectPreferencesGradeDTO;
import com.stegvis_api.stegvis_api.user.dto.AddSubjectPreferencesGradeResponse;
import com.stegvis_api.stegvis_api.user.dto.DeleteUserResponse;
import com.stegvis_api.stegvis_api.user.dto.DeleteUserResult;
import com.stegvis_api.stegvis_api.user.dto.GetUserPreferenceResponse;
import com.stegvis_api.stegvis_api.user.dto.UserProfileResponse;
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
        public ResponseEntity<AddOnboardingPreferencesResponse> updateUserPreferences(
                        @AuthenticationPrincipal UserPrincipal userPrincipal,
                        @RequestBody AddOnboardingPreferencesDTO preferenceDTO) {

                AddOnboardingPreferencesResponse response = userService.setUserPreferences(preferenceDTO,
                                userPrincipal.getId());

                return ResponseEntity.ok(response);
        }

        @PutMapping("/preferences/subjects")
        public ResponseEntity<List<AddSubjectPreferencesResponse>> updateUserSubjectPreferences(
                        @AuthenticationPrincipal UserPrincipal userPrincipal,
                        @RequestBody List<AddSubjectPreferencesDTO> subjectPreferencesDTO) {

                List<AddSubjectPreferencesResponse> responses = userService
                                .setUserSubjectPreferences(subjectPreferencesDTO, userPrincipal.getId());

                return ResponseEntity.ok(responses);
        }

        @PutMapping("/preferences/subject-grades")
        public ResponseEntity<AddSubjectPreferencesGradeResponse> updateUserSubjectGrades(
                        @AuthenticationPrincipal UserPrincipal userPrincipal,
                        @RequestBody List<AddSubjectPreferencesGradeDTO> subjectGradesDTOs) {

                AddSubjectPreferencesGradeResponse response = userService.setUserSubjectGrades(
                                userPrincipal.getId(),
                                subjectGradesDTOs);

                return ResponseEntity.ok(response);
        }

        @PutMapping("/preferences/subject-grades/grade-goal")
        public ResponseEntity<AddGradeGoalResponse> updateUserGradeGoals(
                        @AuthenticationPrincipal UserPrincipal userPrincipal,
                        @RequestBody List<AddGradeGoalDTO> gradeGoalDTOs) {

                AddGradeGoalResponse response = userService.setUserGradeGoal(
                                userPrincipal.getId(),
                                gradeGoalDTOs);

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

        @GetMapping("/me")
        public ResponseEntity<UserProfileResponse> getUserProfileDetails(
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {
                UserProfileResponse profile = userService.getUserProfileDetails(userPrincipal.getId());

                return ResponseEntity.ok(profile);
        }
}