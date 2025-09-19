package com.stegvis_api.stegvis_api.integration.skolverket.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseInfo {

    private String code;
    private String name;
    private String description;
    private CentralContent centralContent;
    private List<KnowledgeRequirement> knowledgeRequirements;

}
