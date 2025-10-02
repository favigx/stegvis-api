package com.stegvis_api.stegvis_api.user.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.stegvis_api.stegvis_api.user.model.User;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByStripeCustomerId(String stripeCustomerId);
}
