package com.stegvis_api.stegvis_api.user.dto;

import com.stegvis_api.stegvis_api.goalplanner.enums.Grade;

public record AddGradeForCurrentDTO(
        String courseCode,
        Grade grade) {
}
