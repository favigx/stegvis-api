package com.stegvis_api.stegvis_api.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDTO {

    @Email(message = "Ogiltig e-postadress")
    @NotBlank(message = "E-post får inte vara tom")
    @NotNull(message = "E-post får inte vara null")
    private String email;

    @NotBlank(message = "Lösenord får inte vara tomt")
    @NotNull(message = "Lösenord får inte vara null")
    @Size(min = 6, message = "Lösenord måste vara minst 6 tecken långt")
    private String password;
}
