package com.stegvis_api.stegvis_api.courses.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stegvis_api.stegvis_api.courses.entities.CourseDTO;
import com.stegvis_api.stegvis_api.courses.model.enums.CourseLevel;
import com.stegvis_api.stegvis_api.courses.model.requests.CreateCourseRequest;
import com.stegvis_api.stegvis_api.courses.model.Course;
import com.stegvis_api.stegvis_api.courses.model.enums.AvailableCourses;
import com.stegvis_api.stegvis_api.courses.services.CoursesService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/courses")
public class CoursesConroller {

    private CoursesService service;

    @GetMapping("/{course}/{level}")
    public ResponseEntity<CourseDTO> getCourseByLevel(@PathVariable AvailableCourses course,
            @PathVariable CourseLevel level) {
        return ResponseEntity.ok(service.getCourseByLevel(course, level));
    }

    @PostMapping()
    public ResponseEntity<Course> createCourse(@RequestBody CreateCourseRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.createCourse(request));
    }

}
