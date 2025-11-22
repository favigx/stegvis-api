package com.stegvis_api.stegvis_api.courses.model;

import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class CourseQuestion {
    @Id
    private final String id;
    private final int index;
    private final String label;
    private final List<CourseAlternative> alternatives;
}
