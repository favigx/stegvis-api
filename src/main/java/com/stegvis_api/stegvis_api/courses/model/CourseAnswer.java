package com.stegvis_api.stegvis_api.courses.model;

import org.springframework.data.annotation.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class CourseAnswer {
    @Id
    private final String id;
    private final String groupId;
    private final String questionId;
    private final String alternativeId;  
}
