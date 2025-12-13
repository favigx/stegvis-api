package com.stegvis_api.stegvis_api.courses.mappers;

import java.util.Objects;

import com.stegvis_api.stegvis_api.courses.model.CourseAlternative;
import com.stegvis_api.stegvis_api.courses.model.CourseQuestion;
import com.stegvis_api.stegvis_api.courses.model.CourseQuestionGroup;
import com.stegvis_api.stegvis_api.courses.model.requests.CourseAlternativeRequest;
import com.stegvis_api.stegvis_api.courses.model.requests.CourseQuestionGroupRequest;
import com.stegvis_api.stegvis_api.courses.model.requests.CourseQuestionRequest;

public class CourseMapper {

    private CourseMapper() {
    }

    public static CourseQuestionGroup mapToCourseQuestionGroup(final CourseQuestionGroupRequest requestGroup) {
        return CourseQuestionGroup.builder()
                .index(requestGroup.getIndex())
                .key(requestGroup.getKey())
                .title(requestGroup.getTitle())
                .contentHtml(requestGroup.getContentHtml())
                .questions(
                        requestGroup.getQuestions() == null
                                ? null
                                : requestGroup.getQuestions()
                                        .stream()
                                        .filter(question -> question != null)
                                        .map(question -> mapToCourseQuestion(question))
                                        .toList())
                .build();
    }

    private static CourseQuestion mapToCourseQuestion(final CourseQuestionRequest requestQuestion) {
        if (requestQuestion == null) {
            return null;
        }

        return CourseQuestion.builder()
                .index(requestQuestion.getIndex())
                .label(requestQuestion.getLabel())
                .alternatives(requestQuestion.getAlternatives()
                        .stream()
                        .map(alternative -> mapToCourseAlternative(alternative))
                        .toList())
                .build();
    }

    private static CourseAlternative mapToCourseAlternative(final CourseAlternativeRequest requestAlternative) {
        return CourseAlternative.builder()
                .label(requestAlternative.getLabel())
                .correct(requestAlternative.isCorrect())
                .build();
    }
}
