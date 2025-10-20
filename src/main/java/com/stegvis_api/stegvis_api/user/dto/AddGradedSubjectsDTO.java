package com.stegvis_api.stegvis_api.user.dto;

public record AddGradedSubjectsDTO(
        String courseName,
        String courseCode,
        int coursePoints) {
}
