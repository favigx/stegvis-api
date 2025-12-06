package com.stegvis_api.stegvis_api.courses.model;

import org.springframework.data.annotation.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class CourseAlternative {
    @Id
    private final String id;
    private final String label;

    private final boolean correct;
}
