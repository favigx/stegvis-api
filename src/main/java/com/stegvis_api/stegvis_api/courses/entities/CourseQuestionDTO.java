package com.stegvis_api.stegvis_api.courses.entities;

import java.util.List;

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
public class CourseQuestionDTO {

    private String id;
    private int index;
    private String label;
    private List<CourseAlternativeDTO> alternatives;
    
}
