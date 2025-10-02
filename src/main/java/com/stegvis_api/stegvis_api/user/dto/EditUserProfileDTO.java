package com.stegvis_api.stegvis_api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditUserProfileDTO {

    private String email;
    private String firstname;
    private String lastname;
}
