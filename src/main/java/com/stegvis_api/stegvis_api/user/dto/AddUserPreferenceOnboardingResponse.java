package com.stegvis_api.stegvis_api.user.dto;

import java.util.List;

import com.stegvis_api.stegvis_api.onboarding.enums.EducationLevel;
import com.stegvis_api.stegvis_api.onboarding.enums.Year;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AddUserPreferenceOnboardingResponse {

    private final String userId;
    private final EducationLevel educationLevel;
    private final String fieldOfStudy;
    private final String orientation;
    private final Year year;
    private final List<String> subjects;

}
