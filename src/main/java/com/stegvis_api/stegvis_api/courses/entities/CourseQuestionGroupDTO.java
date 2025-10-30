package com.stegvis_api.stegvis_api.courses.entities;

import java.util.List;

import com.stegvis_api.stegvis_api.courses.model.CourseQuestion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseQuestionGroupDTO {
    private   String id;
    private  String label;
    private  List<CourseQuestion> questions;
}
