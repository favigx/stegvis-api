package com.stegvis_api.stegvis_api.user.dto;

import com.stegvis_api.stegvis_api.user.model.UserPreference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserPreferenceResponse {
    private String userId;
    private UserPreference userPreference;
}
