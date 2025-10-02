package com.stegvis_api.stegvis_api.auth.dto;

public record UserLoginDTO(
        String email,
        String password) {
}
