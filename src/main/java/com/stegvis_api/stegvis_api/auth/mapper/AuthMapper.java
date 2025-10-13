package com.stegvis_api.stegvis_api.auth.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.stegvis_api.stegvis_api.auth.dto.RefreshTokenResponse;
import com.stegvis_api.stegvis_api.auth.dto.UserLoginResponse;
import com.stegvis_api.stegvis_api.auth.dto.UserRegistrationDTO;
import com.stegvis_api.stegvis_api.auth.dto.UserRegistrationResponse;
import com.stegvis_api.stegvis_api.user.model.User;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "userPreference", ignore = true)
    @Mapping(target = "stripeCustomerId", ignore = true)
    @Mapping(target = "hasCompletedOnboarding", ignore = true)
    @Mapping(target = "googleLinked", ignore = true)
    @Mapping(target = "googleOathCredentials", ignore = true)
    User toUser(UserRegistrationDTO dto);

    UserRegistrationResponse toUserRegistrationResponse(User user);

    UserLoginResponse toUserLoginResponse(User user);

    RefreshTokenResponse toRefreshTokenResponse(User user);
}