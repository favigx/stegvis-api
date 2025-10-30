package com.stegvis_api.stegvis_api.courses.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stegvis_api.stegvis_api.courses.entities.CourseDTO;
import com.stegvis_api.stegvis_api.courses.model.enums.Courses;
import com.stegvis_api.stegvis_api.courses.services.CoursesService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/courses")
public class CoursesConroller {

    private CoursesService service;

    @GetMapping("/{course}")
    public CourseDTO getCourse(@PathVariable Courses course) {
        return service.getCourse(course);
    }

}
