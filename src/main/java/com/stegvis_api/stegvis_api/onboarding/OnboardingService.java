package com.stegvis_api.stegvis_api.onboarding;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.user.model.enums.userpreference.DailyGoal;
import com.stegvis_api.stegvis_api.user.model.enums.userpreference.EducationLevel;
import com.stegvis_api.stegvis_api.user.model.enums.userpreference.FieldOfStudy;
import com.stegvis_api.stegvis_api.user.model.enums.userpreference.FocusDay;
import com.stegvis_api.stegvis_api.user.model.enums.userpreference.HelpRequest;
import com.stegvis_api.stegvis_api.user.model.enums.userpreference.Subject;

@Service
public class OnboardingService {

    public Map<String, Object> getAllEnums() {
        Map<String, Object> enums = new HashMap<>();
        enums.put("educationLevels", mapToTitleCase(EducationLevel.values()));
        enums.put("fieldOfStudies", mapToTitleCase(FieldOfStudy.values()));
        enums.put("subjects", mapToTitleCase(Subject.values()));
        enums.put("focusDays", mapToTitleCase(FocusDay.values()));
        enums.put("dailyGoals", Arrays.stream(DailyGoal.values())
                .map(DailyGoal::getMinutes)
                .toList());
        enums.put("helpRequests", mapToTitleCase(HelpRequest.values()));
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
