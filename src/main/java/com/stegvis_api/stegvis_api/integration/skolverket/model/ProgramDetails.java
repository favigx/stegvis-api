package com.stegvis_api.stegvis_api.integration.skolverket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProgramDetails {

    private String code;
    private String name;
    private SubjectContainer foundationSubjects;
    private SubjectContainer programmeSpecificSubjects;
    private SubjectContainer specialization;
    private List<Orientation> orientations;

}