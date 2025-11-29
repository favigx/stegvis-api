package com.stegvis_api.stegvis_api.courses.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.stegvis_api.stegvis_api.courses.model.CourseQuestionGroup;
import java.util.List;
import com.stegvis_api.stegvis_api.courses.model.enums.Courses;


public interface CourseQuestionGroupRepository extends MongoRepository<CourseQuestionGroup, String>{

    List<CourseQuestionGroup> findByCourse(Courses course);
} 
