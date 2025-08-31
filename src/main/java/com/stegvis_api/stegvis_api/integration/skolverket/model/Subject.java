package com.stegvis_api.stegvis_api.integration.skolverket.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subject {

    private String code;
    private String name;
    private String points;
    private List<Course> courses;

}
