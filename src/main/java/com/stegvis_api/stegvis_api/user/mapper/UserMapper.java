package com.stegvis_api.stegvis_api.user.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.stegvis_api.stegvis_api.user.dto.AddGradeForCurrentDTO;
import com.stegvis_api.stegvis_api.user.dto.AddGradeForCurrentResponse;
import com.stegvis_api.stegvis_api.user.dto.AddGradeGoalDTO;
import com.stegvis_api.stegvis_api.user.dto.AddGradeGoalResponse;
import com.stegvis_api.stegvis_api.user.dto.AddGradedSubjectsDTO;
import com.stegvis_api.stegvis_api.user.dto.AddGradedSubjectsResponse;
import com.stegvis_api.stegvis_api.user.dto.AddOnboardingPreferencesDTO;
import com.stegvis_api.stegvis_api.user.dto.AddOnboardingPreferencesResponse;
import com.stegvis_api.stegvis_api.user.dto.AddSubjectPreferencesDTO;
import com.stegvis_api.stegvis_api.user.dto.AddSubjectPreferencesGradeDTO;
import com.stegvis_api.stegvis_api.user.dto.AddSubjectPreferencesGradeResponse;
import com.stegvis_api.stegvis_api.user.dto.AddSubjectPreferencesResponse;
import com.stegvis_api.stegvis_api.user.dto.UserProfileResponse;
import com.stegvis_api.stegvis_api.user.model.GradedSubject;
import com.stegvis_api.stegvis_api.user.model.SubjectPreference;
import com.stegvis_api.stegvis_api.user.model.User;
import com.stegvis_api.stegvis_api.user.model.UserPreference;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserProfileResponse toUserProfile(User user);

    @Mapping(target = "subjects", ignore = true)
    @Mapping(target = "gradedSubjects", ignore = true)
    @Mapping(target = "meritValue", ignore = true)
    @Mapping(target = "meritValueBasedOnGoal", ignore = true)
    UserPreference toUserPreference(AddOnboardingPreferencesDTO dto);

    AddOnboardingPreferencesResponse toOnboardingPreferencesResponse(UserPreference userPreference);

    @Mapping(target = "grade", ignore = true)
    @Mapping(target = "gradeGoal", ignore = true)
    SubjectPreference toSubjectPreference(AddSubjectPreferencesDTO dto);

    AddSubjectPreferencesResponse toSubjectPreferencesResponse(SubjectPreference subjectPreference);

    List<SubjectPreference> toSubjectPreferences(List<AddSubjectPreferencesDTO> dtos);

    List<AddSubjectPreferencesResponse> toSubjectPreferencesResponses(List<SubjectPreference> subjects);

    @Mapping(target = "courseName", ignore = true)
    @Mapping(target = "coursePoints", ignore = true)
    @Mapping(target = "gradeGoal", ignore = true)
    void updateGradeFromDto(AddSubjectPreferencesGradeDTO dto, @MappingTarget GradedSubject gradedSubject);

    default AddSubjectPreferencesGradeResponse toSubjectPreferencesGradeResponse(
            List<GradedSubject> subjects,
            double meritValue) {
        return new AddSubjectPreferencesGradeResponse(subjects, meritValue);
    }

    @Mapping(target = "courseName", ignore = true)
    @Mapping(target = "coursePoints", ignore = true)
    @Mapping(target = "gradeGoal", ignore = true)
    @Mapping(target = "subjectCode", ignore = true)
    void updateGradeForCurrentFromDto(AddGradeForCurrentDTO dto, @MappingTarget SubjectPreference subjectPreference);

    default AddGradeForCurrentResponse toGradeForCurrentResponse(
            List<SubjectPreference> subjects) {
        return new AddGradeForCurrentResponse(subjects);
    }

    @Mapping(target = "courseName", ignore = true)
    @Mapping(target = "coursePoints", ignore = true)
    @Mapping(target = "grade", ignore = true)
    @Mapping(target = "subjectCode", ignore = true)
    void updateGradeGoalFromDto(AddGradeGoalDTO dto, @MappingTarget SubjectPreference subjectPreference);

    default AddGradeGoalResponse toAddGradeGoalResponse(
            List<SubjectPreference> subjects, double meritValueBasedOnGoal) {
        return new AddGradeGoalResponse(subjects, meritValueBasedOnGoal);
    }

    @Mapping(target = "grade", ignore = true)
    @Mapping(target = "gradeGoal", ignore = true)
    GradedSubject toGradedSubject(AddGradedSubjectsDTO dto);

    AddGradedSubjectsResponse toGradedSubjectResponse(GradedSubject gradedSubject);

    List<GradedSubject> toGradedSubjects(List<AddGradedSubjectsDTO> dtos);

    List<AddGradedSubjectsResponse> toGradedSubjectsResponse(List<GradedSubject> subjects);
}