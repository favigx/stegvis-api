package com.stegvis_api.stegvis_api.courses.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.courses.entities.CourseDTO;
import com.stegvis_api.stegvis_api.courses.model.CourseQuestionGroup;
import com.stegvis_api.stegvis_api.courses.model.enums.Courses;
import com.stegvis_api.stegvis_api.courses.repositories.CourseQuestionGroupRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class CoursesService {

    private CourseQuestionGroupRepository repository;

    public CourseDTO getCourseLevel(final Courses course) {
        List<CourseQuestionGroup> groups = repository.findByCourse(course);

        return CourseDTO.builder()
                .course(course)
                .questiongroups(groups)
                .build();
    }
}
