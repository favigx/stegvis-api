package com.stegvis_api.stegvis_api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegistrationDTO(
        @NotBlank(message = "Förnamn får inte vara tomt") String firstname,
        @NotBlank(message = "Efternamn får inte vara tomt") String lastname,
        @Email(message = "Ogiltig e-postadress") @NotBlank(message = "E-post får inte vara tom") String email,
        @NotBlank(message = "Lösenord får inte vara tomt") @Size(min = 6, message = "Lösenord måste vara minst 6 tecken långt") String password) {
}
