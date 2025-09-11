package com.stegvis_api.stegvis_api.user.dto;

import java.util.List;

import com.stegvis_api.stegvis_api.onboarding.enums.EducationLevel;
import com.stegvis_api.stegvis_api.onboarding.enums.Year;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddUserPreferenceOnboardingDTO {

    private EducationLevel educationLevel;
    private String fieldOfStudy;
    private String orientation;
    private Year year;
    private List<String> subjects;
}
