package com.stegvis_api.stegvis_api.user.model;

import com.stegvis_api.stegvis_api.goalplanner.enums.Grade;

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
public class SubjectPreference {

    private String courseName;
    private String courseCode;
    private int coursePoints;
    private Grade grade;
}
