package com.stegvis_api.stegvis_api.integration.stripe.service;

import com.stegvis_api.stegvis_api.integration.stripe.dto.CreateStripeCustomerDTO;
import com.stegvis_api.stegvis_api.integration.stripe.dto.StripeCheckoutSessionDTO;
import com.stegvis_api.stegvis_api.integration.stripe.dto.StripeSubscriptionMetadataDTO;
import com.stegvis_api.stegvis_api.repository.UserRepository;
import com.stegvis_api.stegvis_api.user.model.User;
import com.stegvis_api.stegvis_api.user.service.UserService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StripeService {

    @Value("${stegvis.stripe.subscriptionPriceId.test}")
    private String stripeSubscriptionPriceIdTest;

    @Value("${stegvis.stripe.succesUrl.test}")
    private String stripeSuccessUrlTest;

    @Value("${stegvis.stripe.cancelUrl.test}")
    private String stripeCancelUrlTest;

    @Value("${stegvis.stripe.apiKey.test}")
    private String stripeApiKeyTest;

    private final UserService userService;
    private final UserRepository userRepository;

    public StripeService(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKeyTest;
        log.info("Stripe API initialized with test key");
    }

    public String createCheckoutSession(String userId) throws StripeException, IOException {
        log.debug("Creating checkout session for userId={}", userId);

        User user = userService.getUserByIdOrThrow(userId);

        if (user.getStripeCustomerId() == null || user.getStripeCustomerId().isEmpty()) {
            log.info("No Stripe customerId found for userId={}, creating new customer", userId);

            CreateStripeCustomerDTO createCustomerDTO = CreateStripeCustomerDTO.builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .build();

            Customer stripeCustomer = createStripeCustomer(createCustomerDTO);

            user.setStripeCustomerId(stripeCustomer.getId());
            userRepository.save(user);

            log.info("Stripe customer created for userId={}, stripeCustomerId={}", userId, stripeCustomer.getId());
        }

        StripeCheckoutSessionDTO sessionDTO = StripeCheckoutSessionDTO.builder()
                .userId(user.getId())
                .stripeCustomerId(user.getStripeCustomerId())
                .build();

        Session session = Session.create(buildSessionParams(sessionDTO));
        log.info("Checkout session created for userId={}, sessionId={}", userId, session.getId());

        return session.getUrl();
    }

    private Customer createStripeCustomer(CreateStripeCustomerDTO dto) throws StripeException {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("userId", dto.getUserId());

        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(dto.getEmail())
                .putAllMetadata(metadata)
                .build();

        try {
            return Customer.create(params);
        } catch (StripeException e) {
            log.error("Failed to create Stripe customer for userId={}: {}", dto.getUserId(), e.getMessage(), e);
            throw e;
        }
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