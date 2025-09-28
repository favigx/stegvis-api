package com.stegvis_api.stegvis_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.stegvis_api.stegvis_api.notes.model.NoteCollection;

public interface NoteCollectionRepository extends MongoRepository<NoteCollection, String> {

    Optional<NoteCollection> findByIdAndUserId(String id, String userId);

    List<NoteCollection> findByUserId(String userId);
}