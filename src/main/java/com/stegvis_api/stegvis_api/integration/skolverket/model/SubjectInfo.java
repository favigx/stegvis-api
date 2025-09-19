package com.stegvis_api.stegvis_api.integration.skolverket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectInfo {

    private String code;
    private String name;
    private String description;
    private String purpose;
}
