package com.stegvis_api.stegvis_api.user.model;

import java.util.List;

import com.stegvis_api.stegvis_api.onboarding.enums.DailyGoal;
import com.stegvis_api.stegvis_api.onboarding.enums.EducationLevel;
import com.stegvis_api.stegvis_api.onboarding.enums.FocusDay;
import com.stegvis_api.stegvis_api.onboarding.enums.HelpRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPreference {

    private EducationLevel educationLevel;
    private String fieldOfStudy;
    private List<String> subjects;
    private List<FocusDay> focusDays;
    private DailyGoal dailyGoal;
    private List<HelpRequest> helpRequests;
}
