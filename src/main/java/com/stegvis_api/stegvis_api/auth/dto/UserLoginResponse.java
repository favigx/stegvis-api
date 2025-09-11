package com.stegvis_api.stegvis_api.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class UserLoginResponse {

    private final String id;
    private final String email;
    private final boolean hasCompletedOnboarding;
}
