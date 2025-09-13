package com.stegvis_api.stegvis_api.integration.stripe.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateStripeCustomerDTO {

    private final String userId;
    private final String email;
}
