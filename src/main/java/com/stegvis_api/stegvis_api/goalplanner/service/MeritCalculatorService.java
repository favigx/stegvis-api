package com.stegvis_api.stegvis_api.goalplanner.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.goalplanner.enums.Grade;
import com.stegvis_api.stegvis_api.user.model.SubjectPreference;
import com.stegvis_api.stegvis_api.user.model.User;
import com.stegvis_api.stegvis_api.user.model.UserPreference;

@Service
public class MeritCalculatorService {

    private static final Map<Grade, Double> GRADE_VALUES = Map.of(
            Grade.A, 20.0,
            Grade.B, 17.5,
            Grade.C, 15.0,
            Grade.D, 12.5,
            Grade.E, 10.0,
            Grade.F, 0.0);

    public double calculateMeritValue(User user) {
        UserPreference preference = user.getUserPreference();
        if (preference == null || preference.getSubjects() == null || preference.getSubjects().isEmpty()) {
            return 0.0;
        }

        return calculateMeritValueFromSubjects(preference.getSubjects());
    }

    public double calculateMeritValueFromSubjects(List<SubjectPreference> subjects) {
        double totalPoints = 0;
        double totalWeighted = 0;

        for (SubjectPreference sp : subjects) {
            if (sp.getGrade() == null)
                continue;

            double gradeValue = GRADE_VALUES.getOrDefault(sp.getGrade(), 0.0);
            double coursePoints = sp.getCoursePoints();

            totalPoints += coursePoints;
            totalWeighted += gradeValue * coursePoints;
        }

        if (totalPoints == 0)
            return 0.0;

        return Math.round((totalWeighted / totalPoints) * 100.0) / 100.0;
    }

    public double calculateMeritValueFromGoals(User user) {
        UserPreference preference = user.getUserPreference();
        if (preference == null || preference.getSubjects() == null || preference.getSubjects().isEmpty()) {
            return 0.0;
        }
        return calculateMeritValueFromGoalSubjects(preference.getSubjects());
    }

    public double calculateMeritValueFromGoalSubjects(List<SubjectPreference> subjects) {
        double totalPoints = 0;
        double totalWeighted = 0;

        for (SubjectPreference sp : subjects) {
            if (sp.getGradeGoal() == null)
                continue;

            double gradeValue = GRADE_VALUES.getOrDefault(sp.getGradeGoal(), 0.0);
            double coursePoints = sp.getCoursePoints();

            totalPoints += coursePoints;
            totalWeighted += gradeValue * coursePoints;
        }

        if (totalPoints == 0)
            return 0.0;

        return Math.round((totalWeighted / totalPoints) * 100.0) / 100.0;
    }
}
