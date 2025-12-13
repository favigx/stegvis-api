package com.stegvis_api.stegvis_api.courses.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.stegvis_api.stegvis_api.courses.model.enums.CourseLevel;
import com.stegvis_api.stegvis_api.courses.model.enums.AvailableCourses;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
@Document(collection = "courses")
public class Course {
    @Id
    private final String id;
    private final AvailableCourses course;
    private final String level;
    private final List<CourseQuestionGroup> questionGroups;
}
