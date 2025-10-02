package com.stegvis_api.stegvis_api.user.dto;

public record UserProfileResponse(
        String email,
        String firstname,
        String lastname) {
}