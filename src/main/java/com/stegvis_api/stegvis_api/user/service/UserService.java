package com.stegvis_api.stegvis_api.user.service;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stegvis_api.stegvis_api.exception.type.ResourceNotFoundException;
import com.stegvis_api.stegvis_api.goalplanner.dto.AddUserSubjectGradesDTO;
import com.stegvis_api.stegvis_api.goalplanner.model.SubjectGrade;
import com.stegvis_api.stegvis_api.goalplanner.service.MeritCalculatorService;
import com.stegvis_api.stegvis_api.repository.NoteRepository;
import com.stegvis_api.stegvis_api.repository.TaskRepository;
import com.stegvis_api.stegvis_api.repository.TodoRepository;
import com.stegvis_api.stegvis_api.repository.UserRepository;
import com.stegvis_api.stegvis_api.user.dto.AddUserPreferenceOnboardingDTO;
import com.stegvis_api.stegvis_api.user.dto.DeleteUserResult;
import com.stegvis_api.stegvis_api.user.model.User;
import com.stegvis_api.stegvis_api.user.model.UserPreference;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final NoteRepository noteRepository;
    private final TaskRepository taskRepository;
    private final TodoRepository todoRepository;
    private final MeritCalculatorService meritCalculatorService;

    public UserService(UserRepository userRepository, NoteRepository noteRepository, TaskRepository taskRepository,
            TodoRepository todoRepository, MeritCalculatorService meritCalculatorService) {
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
        this.taskRepository = taskRepository;
        this.todoRepository = todoRepository;
        this.meritCalculatorService = meritCalculatorService;
    }

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
}