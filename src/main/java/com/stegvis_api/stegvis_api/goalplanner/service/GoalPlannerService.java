package com.stegvis_api.stegvis_api.goalplanner.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.goalplanner.enums.Grade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class GoalPlannerService {

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