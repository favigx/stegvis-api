package com.stegvis_api.stegvis_api.goalplanner.dto;

import java.util.List;

import com.stegvis_api.stegvis_api.goalplanner.model.SubjectGrade;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class AddUserSubjectGradesResponse {

    private final String userId;
    private final List<SubjectGrade> subjectGrades;
}