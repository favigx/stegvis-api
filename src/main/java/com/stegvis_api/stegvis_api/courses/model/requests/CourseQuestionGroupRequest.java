package com.stegvis_api.stegvis_api.courses.model.requests;

import java.util.List;

import lombok.Data;

@Data
public class CourseQuestionGroupRequest {
    private int index;
    private String key;
    private String title;
    private String contentHtml;
    private List<CourseQuestionRequest> questions;
}
