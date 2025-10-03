package com.stegvis_api.stegvis_api.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stegvis_api.stegvis_api.calender.deadline.repository.TaskRepository;
import com.stegvis_api.stegvis_api.exception.type.ResourceNotFoundException;
import com.stegvis_api.stegvis_api.goalplanner.dto.AddUserSubjectGradesDTO;
import com.stegvis_api.stegvis_api.goalplanner.model.SubjectGrade;
import com.stegvis_api.stegvis_api.goalplanner.service.MeritCalculatorService;
import com.stegvis_api.stegvis_api.notes.repository.NoteRepository;
import com.stegvis_api.stegvis_api.onboarding.enums.Year;
import com.stegvis_api.stegvis_api.todo.repository.TodoRepository;
import com.stegvis_api.stegvis_api.user.dto.AddUserPreferenceOnboardingDTO;
import com.stegvis_api.stegvis_api.user.dto.DeleteUserResult;
import com.stegvis_api.stegvis_api.user.dto.UserProfileResponse;
import com.stegvis_api.stegvis_api.user.mapper.UserMapper;
import com.stegvis_api.stegvis_api.user.model.User;
import com.stegvis_api.stegvis_api.user.model.UserPreference;
import com.stegvis_api.stegvis_api.user.repository.UserRepository;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final NoteRepository noteRepository;
    private final TaskRepository taskRepository;
    private final TodoRepository todoRepository;
    private final MeritCalculatorService meritCalculatorService;
    private final UserMapper userMapper;

    @Transactional
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

    @Transactional
    public List<SubjectGrade> setUserSubjectGrades(String userId, AddUserSubjectGradesDTO dto) {
        User user = getUserByIdOrThrow(userId);

        Year year = user.getUserPreference().getYear();

        if (Year.YEAR_1.equals(year)) {
            throw new AccessDeniedException("Users in YEAR_1 are not authorized to set subject grades");
        }

        if (user.getSubjectGrades() == null) {
            user.setSubjectGrades(new ArrayList<>());
        }

        for (SubjectGrade newGrade : dto.getSubjectGrades()) {
            Optional<SubjectGrade> existing = user.getSubjectGrades().stream()
                    .filter(sg -> sg.getSubjectName().equalsIgnoreCase(newGrade.getSubjectName()))
                    .findFirst();

            if (existing.isPresent()) {
                SubjectGrade sg = existing.get();
                sg.setGrade(newGrade.getGrade());
                sg.setCoursePoints(newGrade.getCoursePoints());
            } else {
                user.getSubjectGrades().add(newGrade);
            }
        }

        double meritValue = meritCalculatorService.calculateMeritValue(user.getSubjectGrades());
        user.setMeritValue(meritValue);

        log.debug("Updated subject grades and merit value for user id={}", userId);

        userRepository.save(user);

        return user.getSubjectGrades();
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

    @Transactional
    public DeleteUserResult deleteUser(String userId) {
        User user = getUserByIdOrThrow(userId);

        long deletedNotes = noteRepository.deleteByUserId(userId);
        long deletedTodos = todoRepository.deleteByUserId(userId);
        long deletedTasks = taskRepository.deleteByUserId(userId);

        userRepository.delete(user);

        log.info("User deleted: userId={}, deletedNotes={}, deletedTodos={}, deletedTasks={}",
                userId, deletedNotes, deletedTodos, deletedTasks);

        return new DeleteUserResult(deletedNotes, deletedTodos, deletedTasks);
    }

    public UserProfileResponse getUserProfileDetails(String userId) {
        log.info("Fetching profile for userId={}", userId);

        User user = getUserByIdOrThrow(userId);

        log.info("Successfully fetched profile for userId={}", userId);

        return userMapper.toUserProfile(user);
    }

}