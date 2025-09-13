package com.stegvis_api.stegvis_api.integration.stripe.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StripeCheckoutSessionDTO {

    private final String userId;
    private final String stripeCustomerId;

}
