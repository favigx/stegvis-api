package com.stegvis_api.stegvis_api.user.model;

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
public class UserPreference {

    private EducationLevel educationLevel;
    private ProgramPreference program;
    private OrientationPreference orientation;
    private Year year;
    private List<SubjectPreference> subjects;
}
