package com.stegvis_api.stegvis_api.user.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.stegvis_api.stegvis_api.user.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @Builder.Default
    private Role role = Role.ROLE_USER;

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private UserPreference userPreference;
    private boolean hasCompletedOnboarding;
    private String stripeCustomerId;
}