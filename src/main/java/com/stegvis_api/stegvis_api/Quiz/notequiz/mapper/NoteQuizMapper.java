package com.stegvis_api.stegvis_api.Quiz.notequiz.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.stegvis_api.stegvis_api.Quiz.notequiz.dto.NoteQuizResponse;
import com.stegvis_api.stegvis_api.Quiz.notequiz.model.NoteQuiz;
import com.stegvis_api.stegvis_api.Quiz.notequiz.model.Questions;
import com.stegvis_api.stegvis_api.Quiz.notequiz.model.Options;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface NoteQuizMapper {

    NoteQuizMapper INSTANCE = Mappers.getMapper(NoteQuizMapper.class);

    default NoteQuizResponse toNoteQuizResponse(NoteQuiz noteQuiz) {
        if (noteQuiz == null)
            return null;

        List<NoteQuizResponse.QuestionResponse> questions = noteQuiz.getQuestions().stream()
                .map(this::toQuestionResponse)
                .collect(Collectors.toList());

        return new NoteQuizResponse(
                noteQuiz.getId(),
                noteQuiz.getQuizName(),
                noteQuiz.getCourseName(),
                questions);
    }

    default NoteQuizResponse.QuestionResponse toQuestionResponse(Questions question) {
        if (question == null)
            return null;

        List<NoteQuizResponse.OptionResponse> options = question.getOptions().stream()
                .map(this::toOptionResponse)
                .collect(Collectors.toList());

        return new NoteQuizResponse.QuestionResponse(
                question.getQuestion(),
                options);
    }

    default NoteQuizResponse.OptionResponse toOptionResponse(Options option) {
        if (option == null)
            return null;

        return new NoteQuizResponse.OptionResponse(
                option.getOptionText(),
                option.getPoints(),
                option.isCorrect());
    }

    NoteQuizResponse toGetNoteQuizResponse(NoteQuiz noteQuiz);

    default List<NoteQuizResponse> toNoteQuizResponse(List<NoteQuiz> noteQuizzes) {
        if (noteQuizzes == null)
            return List.of();

        return noteQuizzes.stream()
                .map(this::toNoteQuizResponse)
                .collect(Collectors.toList());
    }

}
