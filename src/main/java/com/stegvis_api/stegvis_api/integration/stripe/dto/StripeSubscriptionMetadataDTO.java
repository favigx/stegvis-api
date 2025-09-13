package com.stegvis_api.stegvis_api.integration.stripe.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Builder
@Getter
@RequiredArgsConstructor
public class StripeSubscriptionMetadataDTO {
    private final String userId;
    private final boolean hasStripeCustomerId;

    public Map<String, String> toMap() {
        return Map.of("userId", userId);
    }
}
