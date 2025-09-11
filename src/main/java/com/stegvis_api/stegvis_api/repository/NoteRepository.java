package com.stegvis_api.stegvis_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.stegvis_api.stegvis_api.notes.model.Note;

public interface NoteRepository extends MongoRepository<Note, String> {
    List<Note> findByUserId(String userId);

    Optional<Note> findByIdAndUserId(String id, String userId);

    List<Note> findByUserIdAndSubject(String userId, String subject);

    long countByUserId(String userId);

    long countByUserIdAndSubject(String userId, String subject);
}
