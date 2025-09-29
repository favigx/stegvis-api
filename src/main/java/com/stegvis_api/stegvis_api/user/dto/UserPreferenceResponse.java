package com.stegvis_api.stegvis_api.user.dto;

import com.stegvis_api.stegvis_api.user.model.UserPreference;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class UserPreferenceResponse {
    private final String userId;
    private final UserPreference userPreference;
}
