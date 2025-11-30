package com.stegvis_api.stegvis_api.courses.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.courses.entities.CourseDTO;
import com.stegvis_api.stegvis_api.courses.mappers.CourseMapper;
import com.stegvis_api.stegvis_api.courses.model.Course;
import com.stegvis_api.stegvis_api.courses.model.CourseAlternative;
import com.stegvis_api.stegvis_api.courses.model.CourseQuestion;
import com.stegvis_api.stegvis_api.courses.model.CourseQuestionGroup;
import com.stegvis_api.stegvis_api.courses.model.enums.CourseLevel;
import com.stegvis_api.stegvis_api.courses.model.requests.CourseAlternativeRequest;
import com.stegvis_api.stegvis_api.courses.model.requests.CourseQuestionGroupRequest;
import com.stegvis_api.stegvis_api.courses.model.requests.CourseQuestionRequest;
import com.stegvis_api.stegvis_api.courses.model.requests.CreateCourseRequest;
import com.stegvis_api.stegvis_api.courses.model.enums.AvailableCourses;
import com.stegvis_api.stegvis_api.courses.repositories.CoursesRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class CoursesService {

    private CoursesRepository repository;

    public CourseDTO getCourseByLevel(final AvailableCourses course, final CourseLevel level) {
        final Course chosenCourse = repository.findByAvailableCoursesAndCourseLevel(course, level);

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
