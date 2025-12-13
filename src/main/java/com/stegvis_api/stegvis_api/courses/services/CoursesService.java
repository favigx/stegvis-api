package com.stegvis_api.stegvis_api.courses.services;

import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.courses.entities.CourseDTO;
import com.stegvis_api.stegvis_api.courses.mappers.CourseMapper;
import com.stegvis_api.stegvis_api.courses.model.Course;
import com.stegvis_api.stegvis_api.courses.model.enums.CourseLevel;
import com.stegvis_api.stegvis_api.courses.model.requests.CreateCourseRequest;
import com.stegvis_api.stegvis_api.courses.model.enums.AvailableCourses;
import com.stegvis_api.stegvis_api.courses.repositories.CoursesRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class CoursesService {

    private final CoursesRepository repository;

    public CourseDTO getCourseByLevel(final AvailableCourses course, final String level) {
        final Course chosenCourse = repository.findByCourseAndLevel(course, level);

        return CourseDTO.builder()
                .course(course)
                .level(level)
                .questionGroups(chosenCourse.getQuestionGroups())
                .build();
    }

    public Course createCourse(final CreateCourseRequest request) {
        final Course course = Course.builder()
                .course(request.getCourse())
                .level(request.getLevel())
                .questionGroups(
                        request.getGroups()
                                .stream()
                                .map(CourseMapper::mapToCourseQuestionGroup)
                                .toList())
                .build();
        final Course saved = repository.save(course);

        return saved;
    }

}
