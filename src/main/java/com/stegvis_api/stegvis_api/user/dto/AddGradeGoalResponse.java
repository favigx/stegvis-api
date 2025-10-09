package com.stegvis_api.stegvis_api.user.dto;

import java.util.List;

import com.stegvis_api.stegvis_api.user.model.SubjectPreference;

public record AddGradeGoalResponse(
        List<SubjectPreference> subjects) {
}
