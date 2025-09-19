package com.stegvis_api.stegvis_api.integration.skolverket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KnowledgeRequirement {
    private String gradeStep;
    private String text;
}