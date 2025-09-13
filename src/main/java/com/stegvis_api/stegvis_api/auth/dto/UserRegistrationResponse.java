package com.stegvis_api.stegvis_api.auth.dto;

import com.stegvis_api.stegvis_api.user.enums.Role;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class UserRegistrationResponse {

    private final String id;
    private final String firstname;
    private final String lastname;
    private final String email;
    private final Role role;
}
