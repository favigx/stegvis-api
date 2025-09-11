package com.stegvis_api.stegvis_api.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class UserRegistrationResponse {
    private final String id;
    private final String fName;
    private final String lName;
    private final String email;
}
