package com.stegvis_api.stegvis_api.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDTO {

    @Email(message = "Ogiltig e-postadress")
    @NotNull(message = "E-post får inte vara null")
    @NotBlank(message = "E-post får inte vara tom")
    private String email;

    @NotBlank(message = "Lösenord får inte vara tomt")
    @NotNull(message = "Lösenord får inte vara null")
    private String password;
}
