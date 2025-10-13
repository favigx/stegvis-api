package com.stegvis_api.stegvis_api.user.model;

import java.util.Set;

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
public class OathCredentials {
    private String provider;
    private String oathId;
    private Set<String> scopes;
}
