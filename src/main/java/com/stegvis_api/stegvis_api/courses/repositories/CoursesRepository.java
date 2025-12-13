package com.stegvis_api.stegvis_api.courses.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.stegvis_api.stegvis_api.courses.model.Course;
import com.stegvis_api.stegvis_api.courses.model.enums.CourseLevel;
import com.stegvis_api.stegvis_api.courses.model.enums.AvailableCourses;

import java.util.List;

public interface CoursesRepository extends MongoRepository<Course, String> {

    Course findByCourseAndLevel(AvailableCourses course, String level);

    List<Course> findAll();
}
