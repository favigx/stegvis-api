package com.stegvis_api.stegvis_api.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.stegvis_api.stegvis_api.auth.dto.RefreshTokenResponse;
import com.stegvis_api.stegvis_api.auth.dto.UserLoginResponse;
import com.stegvis_api.stegvis_api.auth.dto.UserRegistrationDTO;
import com.stegvis_api.stegvis_api.auth.dto.UserRegistrationResponse;
import com.stegvis_api.stegvis_api.user.model.User;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "password", ignore = true)
    User toUserRegistrationDTO(UserRegistrationDTO userRegistrationDTO);

    UserRegistrationResponse toUserRegistrationResponse(User user);

    UserLoginResponse toUserLoginResponse(User user);

    RefreshTokenResponse toRefreshTokenResponse(User user);

}