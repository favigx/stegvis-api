package com.stegvis_api.stegvis_api.integration.stripe.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CheckoutSessionResponse {

    private final String checkoutUrl;

}
