package com.stegvis_api.stegvis_api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthResponse {
    private String id;
    private String user;
    private boolean isAuthenticated;
    private boolean hasCompletedOnboarding;
}
