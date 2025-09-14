package com.stegvis_api.stegvis_api.integration.stripe.service;

import com.stegvis_api.stegvis_api.repository.UserRepository;
import com.stegvis_api.stegvis_api.user.enums.Role;
import com.stegvis_api.stegvis_api.user.model.User;
import com.stegvis_api.stegvis_api.user.service.UserService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.model.Subscription;
import com.stripe.net.Webhook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class StripeWebhookService {

    @Value("${stegvis.stripe.webhook.secret.test}")
    private String stripeEndpointSecret;

    private final UserService userService;
    private final UserRepository userRepository;

    public StripeWebhookService(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public void processEvent(String payload, String sigHeader) {
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, stripeEndpointSecret);
        } catch (SignatureVerificationException e) {
            log.error("Invalid Stripe signature", e);
            throw new IllegalArgumentException("Invalid Stripe signature", e);
        }

        String eventType = event.getType();
        log.info("Received Stripe event: {}", eventType);

        switch (eventType) {
            case "checkout.session.completed":
                handleCheckoutSession(event);
                break;

            case "customer.subscription.deleted":
                handleSubscriptionDeleted(event);
                break;

            default:
                log.warn("Unhandled Stripe event type: {}", eventType);
        }
    }

    @Transactional
    private void handleCheckoutSession(Event event) {
        Session session = deserializeStripeObject(event, Session.class);
        if (session == null)
            return;

        String userId = session.getMetadata().get("userId");
        if (userId == null || userId.isEmpty()) {
            log.warn("No userId found in session metadata for session: {}", session.getId());
            return;
        }

        User user = userService.getUserByIdOrThrow(userId);
        if (user.getRole() != Role.ROLE_PREMIUM_USER) {
            user.setRole(Role.ROLE_PREMIUM_USER);
            userRepository.save(user);
            log.info("Upgraded user {} to PREMIUM.", userId);
        } else {
            log.debug("User {} is already PREMIUM.", userId);
        }
    }

    @Transactional
    private void handleSubscriptionDeleted(Event event) {
        Subscription subscription = deserializeStripeObject(event, Subscription.class);
        if (subscription == null)
            return;

        String customerId = subscription.getCustomer();
        if (customerId == null || customerId.isEmpty()) {
            log.warn("No customerId found in subscription: {}", subscription.getId());
            return;
        }

        User user = userRepository.findByStripeCustomerId(customerId)
                .orElseThrow(() -> new IllegalArgumentException("User not found for customerId " + customerId));

        if (user.getRole() != Role.ROLE_USER) {
            user.setRole(Role.ROLE_USER);
            userRepository.save(user);
            log.info("Downgraded user {} to FREE.", user.getId());
        } else {
            log.debug("User {} is already FREE.", user.getId());
        }
    }

    private <T extends StripeObject> T deserializeStripeObject(Event event, Class<T> clazz) {
        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
        if (deserializer.getObject().isPresent()) {
            StripeObject obj = deserializer.getObject().get();
            if (clazz.isInstance(obj)) {
                return clazz.cast(obj);
            } else {
                log.warn("Unexpected object type: {}", obj.getClass().getName());
            }
        } else {
            log.error("Could not deserialize Stripe event: {} type: {}", event.getId(), event.getType());
        }
        return null;
    }
}