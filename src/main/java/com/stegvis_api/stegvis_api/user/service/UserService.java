package com.stegvis_api.stegvis_api.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stegvis_api.stegvis_api.calender.deadline.repository.TaskRepository;
import com.stegvis_api.stegvis_api.exception.type.ResourceNotFoundException;
import com.stegvis_api.stegvis_api.goalplanner.enums.Grade;
import com.stegvis_api.stegvis_api.goalplanner.service.MeritCalculatorService;
import com.stegvis_api.stegvis_api.notes.repository.NoteRepository;
import com.stegvis_api.stegvis_api.onboarding.enums.Year;
import com.stegvis_api.stegvis_api.todo.repository.TodoRepository;
import com.stegvis_api.stegvis_api.user.dto.AddGradeForCurrentDTO;
import com.stegvis_api.stegvis_api.user.dto.AddGradeForCurrentResponse;
import com.stegvis_api.stegvis_api.user.dto.AddGradeGoalDTO;
import com.stegvis_api.stegvis_api.user.dto.AddGradeGoalResponse;
import com.stegvis_api.stegvis_api.user.dto.AddGradedSubjectsDTO;
import com.stegvis_api.stegvis_api.user.dto.AddGradedSubjectsResponse;
import com.stegvis_api.stegvis_api.user.dto.AddOnboardingPreferencesDTO;
import com.stegvis_api.stegvis_api.user.dto.AddOnboardingPreferencesResponse;
import com.stegvis_api.stegvis_api.user.dto.AddSubjectPreferencesDTO;
import com.stegvis_api.stegvis_api.user.dto.AddSubjectPreferencesResponse;
import com.stegvis_api.stegvis_api.user.dto.AddSubjectPreferencesGradeDTO;
import com.stegvis_api.stegvis_api.user.dto.AddSubjectPreferencesGradeResponse;
import com.stegvis_api.stegvis_api.user.dto.DeleteUserResult;
import com.stegvis_api.stegvis_api.user.dto.UserAuthResponse;
import com.stegvis_api.stegvis_api.user.dto.UserProfileResponse;
import com.stegvis_api.stegvis_api.user.mapper.UserMapper;
import com.stegvis_api.stegvis_api.user.model.GradedSubject;
import com.stegvis_api.stegvis_api.user.model.SubjectPreference;
import com.stegvis_api.stegvis_api.user.model.User;
import com.stegvis_api.stegvis_api.user.model.UserPreference;
import com.stegvis_api.stegvis_api.user.repository.UserRepository;
import java.util.Objects;

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
    public AddOnboardingPreferencesResponse setUserPreferences(AddOnboardingPreferencesDTO dto, String userId) {
        User user = getUserByIdOrThrow(userId);

        UserPreference userPreference = userMapper.toUserPreference(dto);

        user.setUserPreference(userPreference);

        userRepository.save(user);

        log.debug("Set onboarding preferences for user id={}", userId);

        return userMapper.toOnboardingPreferencesResponse(userPreference);
    }

    @Transactional
    public void markOnboardingComplete(String userId) {
        User user = getUserByIdOrThrow(userId);
        user.setHasCompletedOnboarding(true);
        userRepository.save(user);
        log.info("Marked onboarding as complete for user id={}", userId);
    }

    @Transactional
    public List<AddSubjectPreferencesResponse> setUserSubjectPreferences(
            List<AddSubjectPreferencesDTO> dtos, String userId) {

        User user = getUserByIdOrThrow(userId);

        if (user.getUserPreference().getSubjects() == null) {
            user.getUserPreference().setSubjects(new ArrayList<>());
        }

        List<SubjectPreference> incomingSubjects = userMapper.toSubjectPreferences(dtos);

        Set<String> existingCourseCodes = user.getUserPreference().getSubjects()
                .stream()
                .map(SubjectPreference::getCourseCode)
                .collect(Collectors.toSet());

        List<SubjectPreference> subjectsToAdd = incomingSubjects.stream()
                .filter(s -> !existingCourseCodes.contains(s.getCourseCode()))
                .collect(Collectors.toList());

        user.getUserPreference().getSubjects().addAll(subjectsToAdd);
        userRepository.save(user);

        return userMapper.toSubjectPreferencesResponses(subjectsToAdd);
    }

    @Transactional
    public List<AddGradedSubjectsResponse> setUserGradedSubjects(
            List<AddGradedSubjectsDTO> dtos, String userId) {

        User user = getUserByIdOrThrow(userId);

        if (user.getUserPreference().getGradedSubjects() == null) {
            user.getUserPreference().setGradedSubjects(new ArrayList<>());
        }

        List<GradedSubject> incomingSubjects = userMapper.toGradedSubjects(dtos);

        Set<String> existingCourseCodes = user.getUserPreference().getGradedSubjects()
                .stream()
                .map(GradedSubject::getCourseCode)
                .collect(Collectors.toSet());

        List<GradedSubject> subjectsToAdd = incomingSubjects.stream()
                .filter(s -> !existingCourseCodes.contains(s.getCourseCode()))
                .collect(Collectors.toList());

        user.getUserPreference().getGradedSubjects().addAll(subjectsToAdd);
        userRepository.save(user);

        return userMapper.toGradedSubjectsResponse(subjectsToAdd);

    }

    @Transactional
    public AddGradeForCurrentResponse setUserSubjectGradesForCurrent(String userId,
            List<AddGradeForCurrentDTO> dtos) {

        User user = getUserByIdOrThrow(userId);
        UserPreference preference = user.getUserPreference();

        if (preference == null) {
            throw new AccessDeniedException("Du måste först genomföra onboarding och registrera dina preferenser");
        }

        if (Year.YEAR_1.equals(preference.getYear())) {
            throw new AccessDeniedException("Användare i YEAR_1 får inte sätta betyg ännu");
        }

        List<SubjectPreference> subjects = preference.getSubjects();
        if (subjects == null || subjects.isEmpty()) {
            throw new AccessDeniedException("Du måste registrera dina ämnen och kurser innan du kan sätta betyg");
        }

        Map<String, Grade> dtoMap = dtos.stream()
                .collect(Collectors.toMap(AddGradeForCurrentDTO::courseCode, AddGradeForCurrentDTO::grade));

        subjects.forEach(sp -> {
            Grade newGrade = dtoMap.get(sp.getCourseCode());
            if (newGrade != null) {
                sp.setGrade(newGrade);
            }
        });

        List<GradedSubject> newlyGraded = subjects.stream()
                .map(SubjectPreference::toGradedSubject)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (preference.getGradedSubjects() == null) {
            preference.setGradedSubjects(newlyGraded);
        } else {
            preference.getGradedSubjects().addAll(newlyGraded);
        }

        List<SubjectPreference> remainingSubjects = subjects.stream()
                .filter(sp -> sp.getGrade() == null)
                .collect(Collectors.toList());
        preference.setSubjects(remainingSubjects);

        log.info("Updated subject grades for user id={}", userId);

        userRepository.save(user);

        return new AddGradeForCurrentResponse(preference.getSubjects());
    }

    @Transactional
    public AddSubjectPreferencesGradeResponse setUserSubjectGrades(String userId,
            List<AddSubjectPreferencesGradeDTO> dtos) {
        User user = getUserByIdOrThrow(userId);

        UserPreference preference = user.getUserPreference();
        if (preference == null) {
            throw new AccessDeniedException("Du måste först genomföra onboarding och registrera dina preferenser");
        }

        Year year = preference.getYear();
        if (Year.YEAR_1.equals(year)) {
            throw new AccessDeniedException("Användare i YEAR_1 får inte sätta betyg ännu");
        }

        List<GradedSubject> subjects = preference.getGradedSubjects();

        for (AddSubjectPreferencesGradeDTO dto : dtos) {
            subjects.stream()
                    .filter(sp -> sp.getCourseCode().equalsIgnoreCase(dto.courseCode()))
                    .findFirst()
                    .ifPresent(sp -> userMapper.updateGradeFromDto(dto, sp));
        }

        double meritValue = meritCalculatorService.calculateMeritValueFromSubjects(subjects);
        user.getUserPreference().setMeritValue(meritValue);

        log.info("Updated subject grades and merit value for user id={}", userId);

        userRepository.save(user);

        return userMapper.toSubjectPreferencesGradeResponse(subjects, meritValue);
    }

    @Transactional
    public AddGradeGoalResponse setUserGradeGoal(String userId, List<AddGradeGoalDTO> gradeGoalDTOs) {
        User user = getUserByIdOrThrow(userId);

        UserPreference preference = user.getUserPreference();
        if (preference == null) {
            throw new AccessDeniedException("Du måste först genomföra onboarding och registrera dina preferenser");
        }

        List<SubjectPreference> subjects = preference.getSubjects();
        if (subjects == null || subjects.isEmpty()) {
            throw new AccessDeniedException("Du måste registrera dina ämnen och kurser innan du kan sätta betyg");
        }

        for (AddGradeGoalDTO dto : gradeGoalDTOs) {
            subjects.stream()
                    .filter(sp -> sp.getCourseCode().equalsIgnoreCase(dto.courseCode()))
                    .findFirst()
                    .ifPresent(sp -> userMapper.updateGradeGoalFromDto(dto, sp));
        }

        double meritValueBasedOnGoal = meritCalculatorService.calculateMeritValueFromGoalSubjects(subjects);
        user.getUserPreference().setMeritValueBasedOnGoal(meritValueBasedOnGoal);

        log.info("Updated goal for grades for user id={}", userId);

        userRepository.save(user);

        return userMapper.toAddGradeGoalResponse(subjects, meritValueBasedOnGoal);

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

    public UserAuthResponse checkAuth(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserAuthResponse.builder()
                .id(user.getId())
                .user(user.getEmail())
                .isAuthenticated(true)
                .hasCompletedOnboarding(user.isHasCompletedOnboarding())
                .build();
    }
}