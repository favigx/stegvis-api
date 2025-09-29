package com.stegvis_api.stegvis_api.goalplanner.model;

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
public class SubjectGrade {

    private String subjectName;
    private Grade grade;
    private int coursePoints;
}