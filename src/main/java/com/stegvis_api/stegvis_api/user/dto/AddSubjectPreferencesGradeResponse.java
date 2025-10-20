package com.stegvis_api.stegvis_api.user.dto;

import java.util.List;

import com.stegvis_api.stegvis_api.user.model.GradedSubject;

public record AddSubjectPreferencesGradeResponse(
                List<GradedSubject> subjects,
                double meritValue) {
}