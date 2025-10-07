package com.stegvis_api.stegvis_api.user.dto;

import com.stegvis_api.stegvis_api.onboarding.enums.EducationLevel;
import com.stegvis_api.stegvis_api.onboarding.enums.Year;
import com.stegvis_api.stegvis_api.user.model.OrientationPreference;
import com.stegvis_api.stegvis_api.user.model.ProgramPreference;

public record AddOnboardingPreferencesDTO(
        EducationLevel educationLevel,
        ProgramPreference program,
        OrientationPreference orientation,
        Year year) {
}
