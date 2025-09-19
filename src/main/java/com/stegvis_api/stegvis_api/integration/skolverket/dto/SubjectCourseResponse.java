package com.stegvis_api.stegvis_api.integration.skolverket.dto;

import com.stegvis_api.stegvis_api.integration.skolverket.model.CourseInfo;
import com.stegvis_api.stegvis_api.integration.skolverket.model.SubjectInfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SubjectCourseResponse {

    private final SubjectInfo subject;
    private final CourseInfo course;

}
