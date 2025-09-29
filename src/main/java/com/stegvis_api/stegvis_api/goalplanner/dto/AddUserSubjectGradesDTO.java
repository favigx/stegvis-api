package com.stegvis_api.stegvis_api.goalplanner.dto;

import java.util.List;

import com.stegvis_api.stegvis_api.goalplanner.model.SubjectGrade;

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
public class AddUserSubjectGradesDTO {

    private List<SubjectGrade> subjectGrades;
}