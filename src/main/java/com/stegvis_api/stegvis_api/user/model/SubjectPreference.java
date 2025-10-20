package com.stegvis_api.stegvis_api.user.model;

import com.stegvis_api.stegvis_api.goalplanner.enums.Grade;

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
public class SubjectPreference {

    private String subjectCode;
    private String courseName;
    private String courseCode;
    private int coursePoints;
    private Grade grade;
    private Grade gradeGoal;

    public GradedSubject toGradedSubject() {
        if (this.grade == null)
            return null;
        return GradedSubject.builder()
                .courseCode(this.courseCode)
                .courseName(this.courseName)
                .coursePoints(this.coursePoints)
                .grade(this.grade)
                .gradeGoal(this.gradeGoal)
                .build();
    }
}
