package com.stegvis_api.stegvis_api.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.stegvis_api.stegvis_api.notes.model.Note;

public interface NoteRepository extends MongoRepository<Note, String> {
    List<Note> findByUserId(String userId);

    Optional<Note> findByIdAndUserId(String id, String userId);

    List<Note> findByUserIdAndSubject(String userId, String subject);

    @Query("{ 'userId': ?0, 'subject': { $regex: ?1, $options: 'i' }, 'dateTimeCreated': { $gte: ?2, $lte: ?3 } }")
    List<Note> filterNotes(String userId, String subject, Instant from, Instant to);

}
