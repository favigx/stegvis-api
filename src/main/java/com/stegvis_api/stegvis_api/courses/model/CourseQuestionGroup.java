package com.stegvis_api.stegvis_api.courses.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class CourseQuestionGroup {
    private final int index;
    private final String key;
    private final String title;
    private final String contentHtml;
    private final List<CourseQuestion> questions; // set to null if no questions in section
}