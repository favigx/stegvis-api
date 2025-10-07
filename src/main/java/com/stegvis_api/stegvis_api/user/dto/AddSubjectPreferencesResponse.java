package com.stegvis_api.stegvis_api.user.dto;

public record AddSubjectPreferencesResponse(
        String courseName,
        String courseCode,
        int coursePoints) {
}
