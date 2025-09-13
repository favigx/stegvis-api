package com.stegvis_api.stegvis_api.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.exception.type.ResourceNotFoundException;
import com.stegvis_api.stegvis_api.repository.UserRepository;
import com.stegvis_api.stegvis_api.user.dto.AddUserPreferenceOnboardingDTO;
import com.stegvis_api.stegvis_api.user.model.User;
import com.stegvis_api.stegvis_api.user.model.UserPreference;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserPreference setUserPreferences(String userId, AddUserPreferenceOnboardingDTO dto) {
        User user = getUserByIdOrThrow(userId);

        UserPreference preference = UserPreference.builder()
                .educationLevel(dto.getEducationLevel())
                .fieldOfStudy(dto.getFieldOfStudy())
                .orientation(dto.getOrientation())
                .year(dto.getYear())
                .subjects(dto.getSubjects())
                .build();

        user.setUserPreference(preference);
        user.setHasCompletedOnboarding(true);

        userRepository.save(user);

        log.debug("Set onboarding preferences for user id={}", userId);
        return preference;
    }

    public UserPreference getUserPreferences(String userId) {
        User user = getUserByIdOrThrow(userId);
        log.debug("Fetched onboarding preferences for user id={}", userId);
        return user.getUserPreference();
    }

    public User getUserByIdOrThrow(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User not found: id={}", userId);
                    return new ResourceNotFoundException("Användaren med id: " + userId + " hittades inte");
                });
    }
}