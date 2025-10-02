package com.stegvis_api.stegvis_api.goalplanner.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.exception.type.ResourceNotFoundException;
import com.stegvis_api.stegvis_api.goalplanner.enums.Grade;
import com.stegvis_api.stegvis_api.goalplanner.model.SubjectGrade;
import com.stegvis_api.stegvis_api.user.model.User;
import com.stegvis_api.stegvis_api.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class GoalPlannerService {

    private final UserService userService;
    private final MeritCalculatorService meritCalculatorService;

    public double calculateMeriteValueForUser(String userId) {
        log.info("Starting merit value calculation for user with id={}", userId);

        User user = userService.getUserByIdOrThrow(userId);

        List<SubjectGrade> subjectGrades = user.getSubjectGrades();

        if (subjectGrades == null || subjectGrades.isEmpty()) {
            log.warn("No grades set for user with id={}", userId);
            throw new ResourceNotFoundException("Inga betyg är satta för användare" + userId);
        }

        return meritCalculatorService.calculateMeritValue(subjectGrades);
    }

    public Map<String, Object> getGradeEnums() {
        Map<String, Object> enums = new HashMap<>();
        enums.put("grades", mapToTitleCase(Grade.values()));

        log.debug("Retrieved grade enums: {}", enums.keySet());
        return enums;
    }

    private <E extends Enum<E>> List<String> mapToTitleCase(E[] enumValues) {
        return Arrays.stream(enumValues)
                .map(this::toTitleCase)
                .toList();
    }

    private <E extends Enum<E>> String toTitleCase(E e) {
        String s = e.name().toLowerCase();
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}