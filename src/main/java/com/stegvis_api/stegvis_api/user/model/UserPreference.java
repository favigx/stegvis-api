package com.stegvis_api.stegvis_api.user.model;

import java.util.List;

import com.stegvis_api.stegvis_api.onboarding.enums.DailyGoal;
import com.stegvis_api.stegvis_api.onboarding.enums.EducationLevel;
import com.stegvis_api.stegvis_api.onboarding.enums.FocusDay;
import com.stegvis_api.stegvis_api.onboarding.enums.HelpRequest;
import com.stegvis_api.stegvis_api.onboarding.enums.Year;
import com.stegvis_api.stegvis_api.onboarding.model.OnboardingSubject;

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
    private String fieldOfStudy;
    private String orientation;
    private Year year;
    private List<OnboardingSubject> subjects;
    private List<FocusDay> focusDays;
    private DailyGoal dailyGoal;
    private List<HelpRequest> helpRequests;
}
