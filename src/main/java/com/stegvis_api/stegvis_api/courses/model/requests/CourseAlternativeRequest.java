package com.stegvis_api.stegvis_api.courses.model.requests;

import lombok.Data;

@Data
public class CourseAlternativeRequest {
    private String label;
    private boolean correct;
}
