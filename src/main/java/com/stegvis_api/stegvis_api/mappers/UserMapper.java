package com.stegvis_api.stegvis_api.mappers;

import org.mapstruct.Mapper;

import com.stegvis_api.stegvis_api.user.dto.UserProfileResponse;
import com.stegvis_api.stegvis_api.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserProfileResponse toUserProfile(User user);
}
