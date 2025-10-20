package com.stegvis_api.stegvis_api.Quiz.notequiz.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.stegvis_api.stegvis_api.Quiz.notequiz.dto.NoteQuizResponse;
import com.stegvis_api.stegvis_api.Quiz.notequiz.service.NoteQuizService;
import com.stegvis_api.stegvis_api.config.security.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/quiz/note")
@RequiredArgsConstructor
public class NoteQuizController {

    private final NoteQuizService noteQuizService;

    @PostMapping("/{noteId}/generate")
    public ResponseEntity<NoteQuizResponse> createQuiz(
            @PathVariable String noteId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) throws Exception {
        NoteQuizResponse response = noteQuizService.generateNoteQuiz(noteId, userPrincipal.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{noteQuizId}")
    public ResponseEntity<NoteQuizResponse> getQuizById(
            @PathVariable String noteQuizId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        NoteQuizResponse response = noteQuizService.getNoteQuizById(userPrincipal.getId(), noteQuizId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<NoteQuizResponse>> getAllUserQuizzes(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<NoteQuizResponse> response = noteQuizService.getAllNoteQuizzesForUser(userPrincipal.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-course")
    public ResponseEntity<List<NoteQuizResponse>> getAllUserQuizzesByCourseName(
            @RequestParam String courseName,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        List<NoteQuizResponse> response = noteQuizService
                .getAllNoteQuizzesForUserByCourseName(userPrincipal.getId(), courseName);

        return ResponseEntity.ok(response);
    }

}
