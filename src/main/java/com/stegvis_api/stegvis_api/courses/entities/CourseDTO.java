package com.stegvis_api.stegvis_api.courses.entities;

import java.util.List;

import com.stegvis_api.stegvis_api.courses.model.CourseQuestionGroup;
import com.stegvis_api.stegvis_api.courses.model.enums.Courses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CourseDTO {
    private final Courses course;
    private final List<CourseQuestionGroup> questiongroups;

}
