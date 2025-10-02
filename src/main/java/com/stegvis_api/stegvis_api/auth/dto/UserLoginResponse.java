package com.stegvis_api.stegvis_api.auth.dto;

public record UserLoginResponse(
        String id,
        String email,
        boolean hasCompletedOnboarding) {
}