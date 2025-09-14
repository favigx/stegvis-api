package com.stegvis_api.stegvis_api.integration.stripe.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stegvis_api.stegvis_api.config.security.UserPrincipal;
import com.stegvis_api.stegvis_api.integration.stripe.dto.CheckoutSessionResponse;
import com.stegvis_api.stegvis_api.integration.stripe.service.StripeCheckoutService;
import com.stripe.exception.StripeException;

@RestController
@RequestMapping("/api/stripe")
public class StripeController {

    private final StripeCheckoutService stripeCheckoutService;

    public StripeController(StripeCheckoutService stripeCheckoutService) {
        this.stripeCheckoutService = stripeCheckoutService;
    }

    @GetMapping("/checkoutsession")
    public ResponseEntity<CheckoutSessionResponse> createCheckoutSession(
            @AuthenticationPrincipal UserPrincipal userPrincipal) throws StripeException, IOException {

        String checkoutUrl = stripeCheckoutService.createCheckoutSession(userPrincipal.getId());

        return ResponseEntity.ok(new CheckoutSessionResponse(checkoutUrl));
    }
}