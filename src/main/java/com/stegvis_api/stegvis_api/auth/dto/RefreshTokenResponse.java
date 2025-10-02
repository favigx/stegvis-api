package com.stegvis_api.stegvis_api.auth.dto;

public record RefreshTokenResponse(
        String id,
        String email) {
}