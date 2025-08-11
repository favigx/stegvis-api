package com.stegvis_api.stegvis_api.user.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPreference {

    private String educationLevel;
    private String fieldOfStudy;
    private List<String> subjects;
    private List<String> focusDays;
    private String helpRequest;
}
