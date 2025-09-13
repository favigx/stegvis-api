package com.stegvis_api.stegvis_api.onboarding.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.onboarding.enums.DailyGoal;
import com.stegvis_api.stegvis_api.onboarding.enums.EducationLevel;
import com.stegvis_api.stegvis_api.onboarding.enums.FocusDay;
import com.stegvis_api.stegvis_api.onboarding.enums.Grades;
import com.stegvis_api.stegvis_api.onboarding.enums.HelpRequest;
import com.stegvis_api.stegvis_api.onboarding.enums.Year;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OnboardingService {

    public Map<String, Object> getAllEnums() {
        Map<String, Object> enums = new HashMap<>();
        enums.put("educationLevels", mapToTitleCase(EducationLevel.values()));
        enums.put("grades", mapToTitleCase(Grades.values()));
        enums.put("focusDays", mapToTitleCase(FocusDay.values()));
        enums.put("dailyGoals", Arrays.stream(DailyGoal.values())
                .map(DailyGoal::getMinutes)
                .toList());
        enums.put("year", Arrays.stream(Year.values())
                .map(Year::getYear)
                .toList());
        enums.put("helpRequests", mapToTitleCase(HelpRequest.values()));

        log.debug("Retrieved onboarding enums: {}", enums.keySet());
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