package com.stegvis_api.stegvis_api.courses.entities;

import java.util.List;

import com.stegvis_api.stegvis_api.courses.model.CourseQuestionGroup;
import com.stegvis_api.stegvis_api.courses.model.enums.CourseLevel;
import com.stegvis_api.stegvis_api.courses.model.enums.AvailableCourses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CourseDTO {
    private final AvailableCourses course;
    private final String level;
    private final List<CourseQuestionGroup> questionGroups;
}
