package com.stegvis_api.stegvis_api.courses.model.requests;

import java.util.List;

import com.stegvis_api.stegvis_api.courses.model.enums.AvailableCourses;
import com.stegvis_api.stegvis_api.courses.model.enums.CourseLevel;

import lombok.Data;

@Data
public class CreateCourseRequest {
    private AvailableCourses course;
    private CourseLevel level;
    private List<CourseQuestionGroupRequest> groups;
}
