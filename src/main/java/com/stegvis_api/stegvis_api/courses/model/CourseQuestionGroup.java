package com.stegvis_api.stegvis_api.courses.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.stegvis_api.stegvis_api.courses.model.enums.Courses;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
@Document(collection = "question_groups")
public class CourseQuestionGroup {
    @Id
    private final String id;
    private final Courses course;
    private final long level;
    private final String label;
    private final boolean hasQuestions;
    private final Optional<List<CourseQuestion>> questions;
}