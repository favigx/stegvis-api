package com.stegvis_api.stegvis_api.Quiz.notequiz.dto;

import java.util.List;

public record NoteQuizResponse(
        String id,
        String quizName,
        String courseName,
        List<QuestionResponse> questions) {
    public record QuestionResponse(
            String question,
            List<OptionResponse> options) {
    }

    public record OptionResponse(
            String optionText,
            int points,
            boolean correct) {
    }
}
