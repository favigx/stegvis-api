package com.stegvis_api.stegvis_api.courses.model.requests;

import java.util.List;

import lombok.Data;

@Data
public class CourseQuestionRequest {
    private int index;
    private String label;
    private List<CourseAlternativeRequest> alternatives;
}
