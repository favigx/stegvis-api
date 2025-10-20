package com.stegvis_api.stegvis_api.user.dto;

public record AddGradedSubjectsResponse(
        String courseName,
        String courseCode,
        int coursePoints) {
}
