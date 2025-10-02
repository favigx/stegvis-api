package com.stegvis_api.stegvis_api.integration.stripe.service;

import com.stegvis_api.stegvis_api.integration.stripe.dto.StripeCheckoutSessionDTO;
import com.stegvis_api.stegvis_api.integration.stripe.dto.StripeSubscriptionMetadataDTO;
import com.stegvis_api.stegvis_api.user.model.User;
import com.stegvis_api.stegvis_api.user.service.UserService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class StripeCheckoutService {

    @Value("${stegvis.stripe.subscriptionPriceId.test}")
    private String stripeSubscriptionPriceIdTest;

    @Value("${stegvis.stripe.succesUrl.test}")
    private String stripeSuccessUrlTest;

    @Value("${stegvis.stripe.cancelUrl.test}")
    private String stripeCancelUrlTest;

    @Value("${stegvis.stripe.apiKey.test}")
    private String stripeApiKeyTest;

    private final UserService userService;
    private final StripeCustomerService stripeCustomerService;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKeyTest;
        log.info("Stripe API initialized with test key");
    }

    @Transactional
    public String createCheckoutSession(String userId) throws StripeException {
        log.debug("Creating checkout session for userId={}", userId);

        User user = userService.getUserByIdOrThrow(userId);

        Customer stripeCustomer = stripeCustomerService.createCustomerForUser(userId);

        StripeCheckoutSessionDTO sessionDTO = StripeCheckoutSessionDTO.builder()
                .userId(user.getId())
                .stripeCustomerId(stripeCustomer.getId())
                .build();

        Session session = Session.create(buildSessionParams(sessionDTO));
        log.info("Checkout session created for userId={}, sessionId={}", userId, session.getId());

        return session.getUrl();
    }

    private SessionCreateParams.SubscriptionData buildSubscriptionData(StripeSubscriptionMetadataDTO dto) {
        SessionCreateParams.SubscriptionData.Builder builder = SessionCreateParams.SubscriptionData.builder()
                .putAllMetadata(dto.toMap());

        if (!dto.isHasStripeCustomerId()) {
            log.debug("Applying trial period for new userId={}", dto.getUserId());
            builder.setTrialPeriodDays(7L);
        }

        return builder.build();
    }

    private SessionCreateParams buildSessionParams(StripeCheckoutSessionDTO dto) {
        StripeSubscriptionMetadataDTO metadataDTO = StripeSubscriptionMetadataDTO.builder()
                .userId(dto.getUserId())
                .hasStripeCustomerId(dto.getStripeCustomerId() != null && !dto.getStripeCustomerId().isEmpty())
                .build();

        SessionCreateParams.Builder builder = SessionCreateParams.builder()
                .setSuccessUrl(stripeSuccessUrlTest)
                .setCancelUrl(stripeCancelUrlTest)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPrice(stripeSubscriptionPriceIdTest)
                        .build())
                .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                .setSubscriptionData(buildSubscriptionData(metadataDTO))
                .putMetadata("userId", dto.getUserId());

        if (dto.getStripeCustomerId() != null && !dto.getStripeCustomerId().isEmpty()) {
            builder.setCustomer(dto.getStripeCustomerId());
            log.debug("Using existing Stripe customerId={} for userId={}", dto.getStripeCustomerId(), dto.getUserId());
        }

        return builder.build();
    }
}