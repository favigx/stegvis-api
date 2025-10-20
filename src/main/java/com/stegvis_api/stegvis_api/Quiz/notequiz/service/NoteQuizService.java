package com.stegvis_api.stegvis_api.Quiz.notequiz.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stegvis_api.stegvis_api.Quiz.notequiz.dto.NoteQuizResponse;
import com.stegvis_api.stegvis_api.Quiz.notequiz.mapper.NoteQuizMapper;
import com.stegvis_api.stegvis_api.Quiz.notequiz.model.NoteQuiz;
import com.stegvis_api.stegvis_api.Quiz.notequiz.model.Questions;
import com.stegvis_api.stegvis_api.integration.openai.service.OpenAiNoteQuizService;
import com.stegvis_api.stegvis_api.notes.model.Note;
import com.stegvis_api.stegvis_api.notes.repository.NoteRepository;
import com.stegvis_api.stegvis_api.user.service.UserService;
import com.stegvis_api.stegvis_api.Quiz.notequiz.repository.NoteQuizRepository;
import com.stegvis_api.stegvis_api.exception.type.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoteQuizService {

    private final NoteRepository noteRepository;
    private final NoteQuizRepository noteQuizRepository;
    private final NoteQuizMapper noteQuizMapper;
    private final OpenAiNoteQuizService openAiNoteQuizService;
    private final ObjectMapper objectMapper;
    private final UserService userService;

    @Transactional
    public NoteQuizResponse generateNoteQuiz(String noteId, String userId) throws Exception {

        Note note = noteRepository.findByIdAndUserId(noteId, userId)
                .orElseThrow(() -> new RuntimeException("Note hittades inte för användare"));

        String aiQuizContent = openAiNoteQuizService.generateNoteQuiz(note);

        List<Questions> questions = parseAiQuizContent(aiQuizContent);

        NoteQuiz noteQuiz = new NoteQuiz();
        noteQuiz.setUserId(userId);
        noteQuiz.setQuizName("Quiz för: " + note.getSubject());
        noteQuiz.setCourseName(note.getSubject());
        noteQuiz.setQuestions(questions);

        NoteQuiz savedQuiz = noteQuizRepository.save(noteQuiz);

        return noteQuizMapper.toNoteQuizResponse(savedQuiz);
    }

    private List<Questions> parseAiQuizContent(String aiQuizContent) throws Exception {
        if (aiQuizContent == null || aiQuizContent.isBlank()) {
            throw new IllegalArgumentException("AI-svaret är tomt");
        }

        int startIndex = aiQuizContent.indexOf('[');
        int endIndex = aiQuizContent.lastIndexOf(']');

        if (startIndex == -1 || endIndex == -1 || startIndex > endIndex) {
            throw new IllegalArgumentException("AI-svaret innehåller ingen giltig JSON-lista");
        }

        String json = aiQuizContent.substring(startIndex, endIndex + 1);

        try {
            return objectMapper.readValue(json, new TypeReference<List<Questions>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Misslyckades med att parsa AI JSON-svar", e);
        }
    }

    public NoteQuizResponse getNoteQuizById(String userId, String noteQuizId) {
        userService.getUserByIdOrThrow(userId);

        NoteQuiz noteQuiz = noteQuizRepository.findByIdAndUserId(noteQuizId, userId)
                .orElseThrow(() -> {
                    log.warn("Note not found: noteId={} for userId={}", noteQuizId, userId);
                    return new ResourceNotFoundException("NoteQuiz not found");
                });
        return noteQuizMapper.toGetNoteQuizResponse(noteQuiz);
    }

    public List<NoteQuizResponse> getAllNoteQuizzesForUser(String userId) {
        userService.getUserByIdOrThrow(userId);

        List<NoteQuiz> noteQuizzes = noteQuizRepository.findByUserId(userId);
        log.debug("Fetched {} notes for userId={}", noteQuizzes.size(), userId);

        return noteQuizMapper.toNoteQuizResponse(noteQuizzes);
    }

    public List<NoteQuizResponse> getAllNoteQuizzesForUserByCourseName(String userId, String courseName) {
        userService.getUserByIdOrThrow(userId);

        List<NoteQuiz> noteQuizzes = noteQuizRepository.findByUserIdAndCourseName(userId, courseName);
        log.debug("Fetched {} notes for userId={} with courseName={}", noteQuizzes.size(), userId, courseName);

        return noteQuizMapper.toNoteQuizResponse(noteQuizzes);
    }
}