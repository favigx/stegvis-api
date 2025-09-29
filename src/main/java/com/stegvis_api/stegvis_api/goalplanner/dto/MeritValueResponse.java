package com.stegvis_api.stegvis_api.goalplanner.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class MeritValueResponse {

    private final String userId;
    private final double meritValue;
}
