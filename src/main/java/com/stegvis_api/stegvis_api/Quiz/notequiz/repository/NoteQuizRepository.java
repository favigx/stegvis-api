package com.stegvis_api.stegvis_api.Quiz.notequiz.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.stegvis_api.stegvis_api.Quiz.notequiz.model.NoteQuiz;

public interface NoteQuizRepository extends MongoRepository<NoteQuiz, String> {
    Optional<NoteQuiz> findByIdAndUserId(String id, String userId);

    List<NoteQuiz> findByUserId(String userId);

    List<NoteQuiz> findByUserIdAndCourseName(String userId, String courseName);

}
