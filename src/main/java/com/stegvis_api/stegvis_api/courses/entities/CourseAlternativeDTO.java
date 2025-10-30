package com.stegvis_api.stegvis_api.courses.entities;

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
public class CourseAlternativeDTO {
    private String id;
    private String answer; 
}
