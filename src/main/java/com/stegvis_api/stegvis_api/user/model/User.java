package com.stegvis_api.stegvis_api.user.model;

import org.springframework.data.annotation.Id;

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
public class User {

    @Id
    private String id;

    private String fName;
    private String lName;
    private String email;
    private String username;
    private String password;
    private UserPreference userPreference;
    private boolean hasCompletedOnboarding;
    private String stripeCustomerId;
}
