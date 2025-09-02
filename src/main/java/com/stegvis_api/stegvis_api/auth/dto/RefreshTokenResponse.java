package com.stegvis_api.stegvis_api.auth.dto;

import lombok.Getter;

import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RefreshTokenResponse {
    private final String id;
    private final String email;
}
