package com.stegvis_api.stegvis_api.integration.stripe.service;

import com.stegvis_api.stegvis_api.repository.UserRepository;
import com.stegvis_api.stegvis_api.user.model.User;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class StripeCustomerService {

    private final UserRepository userRepository;

    public StripeCustomerService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Customer createCustomerForUser(String userId) throws StripeException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        if (user.getStripeCustomerId() != null && !user.getStripeCustomerId().isEmpty()) {
            log.debug("User {} already has a Stripe customerId={}", user.getId(), user.getStripeCustomerId());
            return Customer.retrieve(user.getStripeCustomerId());
        }

        Map<String, String> metadata = new HashMap<>();
        metadata.put("userId", user.getId());

        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(user.getEmail())
                .putAllMetadata(metadata)
                .build();

        Customer customer = Customer.create(params);
        user.setStripeCustomerId(customer.getId());
        userRepository.save(user);

        log.info("Created Stripe customer for userId={}, stripeCustomerId={}", user.getId(), customer.getId());
        return customer;
    }
}
