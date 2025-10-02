package com.stegvis_api.stegvis_api.auth.dto;

import com.stegvis_api.stegvis_api.user.enums.Role;

public record UserRegistrationResponse(
        String id,
        String firstname,
        String lastname,
        String email,
        Role role) {
}
