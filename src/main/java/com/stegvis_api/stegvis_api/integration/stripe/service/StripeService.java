package com.stegvis_api.stegvis_api.integration.stripe.service;

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
    }

    public String createCheckoutSession(String userId) throws StripeException, IOException {
        User user = userService.getUserByIdOrThrow(userId);

        if (user.getStripeCustomerId() == null || user.getStripeCustomerId().isEmpty()) {
            Customer stripeCustomer = createStripeCustomer(user);
            user.setStripeCustomerId(stripeCustomer.getId());
            userRepository.save(user);
        }

        Session session = Session.create(buildSessionParams(user));
        return session.getUrl();
    }

    private Customer createStripeCustomer(User user) throws StripeException {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("userId", user.getId());

        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(user.getEmail())
                .putAllMetadata(metadata)
                .build();

        return Customer.create(params);
    }

    private Map<String, String> buildSubscriptionMetadata(User user) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("userId", user.getId());
        return metadata;
    }

    private SessionCreateParams.SubscriptionData buildSubscriptionData(User user) {
        SessionCreateParams.SubscriptionData.Builder builder = SessionCreateParams.SubscriptionData.builder()
                .putAllMetadata(buildSubscriptionMetadata(user));

        if (user.getStripeCustomerId() == null || user.getStripeCustomerId().isEmpty()) {
            builder.setTrialPeriodDays(7L);
        }

        return builder.build();
    }

    private SessionCreateParams buildSessionParams(User user) {
        SessionCreateParams.Builder builder = SessionCreateParams.builder()
                .setSuccessUrl(stripeSuccessUrlTest)
                .setCancelUrl(stripeCancelUrlTest)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.LINK)
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPrice(stripeSubscriptionPriceIdTest)
                        .build())
                .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                .setSubscriptionData(buildSubscriptionData(user));

        if (user.getStripeCustomerId() != null && !user.getStripeCustomerId().isEmpty()) {
            builder.setCustomer(user.getStripeCustomerId());
        }

        return builder.build();
    }
}