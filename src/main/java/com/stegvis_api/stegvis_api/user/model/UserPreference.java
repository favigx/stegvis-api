package com.stegvis_api.stegvis_api.user.model;

import java.util.List;

import com.stegvis_api.stegvis_api.user.model.enums.userpreference.DailyGoal;
import com.stegvis_api.stegvis_api.user.model.enums.userpreference.EducationLevel;
import com.stegvis_api.stegvis_api.user.model.enums.userpreference.FieldOfStudy;
import com.stegvis_api.stegvis_api.user.model.enums.userpreference.FocusDay;
import com.stegvis_api.stegvis_api.user.model.enums.userpreference.HelpRequest;
import com.stegvis_api.stegvis_api.user.model.enums.userpreference.Subject;

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
    private FieldOfStudy fieldOfStudy;
    private List<Subject> subjects;
    private List<FocusDay> focusDays;
    private DailyGoal dailyGoal;
    private List<HelpRequest> helpRequests;
}
