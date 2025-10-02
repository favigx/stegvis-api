package com.stegvis_api.stegvis_api.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class EditUserResponse {

    private final String userId;
    private final String email;
    private final String firstname;
    private final String lastname;
}
