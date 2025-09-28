package com.stegvis_api.stegvis_api.notes.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stegvis_api.stegvis_api.exception.type.ResourceNotFoundException;
import com.stegvis_api.stegvis_api.notes.dto.AddNoteCollectionDTO;
import com.stegvis_api.stegvis_api.notes.dto.AddNoteToCollectionDTO;
import com.stegvis_api.stegvis_api.notes.dto.AddNoteToCollectionResponse;
import com.stegvis_api.stegvis_api.notes.model.Note;
import com.stegvis_api.stegvis_api.notes.model.NoteCollection;
import com.stegvis_api.stegvis_api.repository.NoteCollectionRepository;
import com.stegvis_api.stegvis_api.repository.NoteRepository;
import com.stegvis_api.stegvis_api.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NoteCollectionService {

    private final NoteCollectionRepository noteCollectionRepository;
    private final NoteRepository noteRepository;
    private final UserService userService;

    public NoteCollectionService(NoteCollectionRepository noteCollectionRepository, NoteRepository noteRepository,
            UserService userService) {
        this.noteCollectionRepository = noteCollectionRepository;
        this.noteRepository = noteRepository;
        this.userService = userService;
    }

    @Transactional
    public NoteCollection createNoteCollection(AddNoteCollectionDTO noteCollectionDTO, String userId) {
        userService.getUserByIdOrThrow(userId);

        NoteCollection noteCollection = NoteCollection.builder()
                .userId(userId)
                .name(noteCollectionDTO.getName())
                .notes(new ArrayList<>())
                .build();

        NoteCollection savedNoteCollection = noteCollectionRepository.save(noteCollection);
        log.info("NoteCollection created for userId={}, noteCollectionId={}", userId, savedNoteCollection.getId());
        return savedNoteCollection;
    }

    @Transactional
    public AddNoteToCollectionResponse addNoteToCollection(AddNoteToCollectionDTO dto, String userId) {
        userService.getUserByIdOrThrow(userId);

        Note note = noteRepository.findByIdAndUserId(dto.getNoteId(), userId)
                .orElseThrow(() -> {
                    log.warn("Add failed: noteId={} not found for userId={}", dto.getNoteId(), userId);
                    return new ResourceNotFoundException("Note hittades inte för användare");
                });

        NoteCollection collection = noteCollectionRepository.findByIdAndUserId(dto.getCollectionId(), userId)
                .orElseThrow(() -> {
                    log.warn("Add failed: collectionId={} not found for userId={}", dto.getCollectionId(), userId);
                    return new ResourceNotFoundException("Collection hittades inte för användare");
                });

        collection.addNote(note);
        noteCollectionRepository.save(collection);

        log.info("Note med id={} tillagd i collection={} för userId={}", dto.getNoteId(), dto.getCollectionId(),
                userId);

        return AddNoteToCollectionResponse.builder()
                .id(collection.getId())
                .name(collection.getName())
                .noteId(note.getId())
                .noteCount(collection.getNotes().size())
                .build();
    }

    public NoteCollection getNoteCollectionWithNotes(String collectionId, String userId) {
        userService.getUserByIdOrThrow(userId);

        return noteCollectionRepository.findByIdAndUserId(collectionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Collection hittades inte för userId=" + userId));
    }

    public List<NoteCollection> getAllNoteCollections(String userId) {
        userService.getUserByIdOrThrow(userId);

        return noteCollectionRepository.findByUserId(userId);
    }
}