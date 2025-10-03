package com.stegvis_api.stegvis_api.notes.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stegvis_api.stegvis_api.exception.type.ResourceNotFoundException;
import com.stegvis_api.stegvis_api.notes.dto.AddNoteCollectionDTO;
import com.stegvis_api.stegvis_api.notes.dto.AddNoteCollectionResponse;
import com.stegvis_api.stegvis_api.notes.dto.AddNoteToCollectionDTO;
import com.stegvis_api.stegvis_api.notes.dto.AddNoteToCollectionResponse;
import com.stegvis_api.stegvis_api.notes.dto.NoteCollectionResponse;
import com.stegvis_api.stegvis_api.notes.mapper.NoteMapper;
import com.stegvis_api.stegvis_api.notes.model.Note;
import com.stegvis_api.stegvis_api.notes.model.NoteCollection;
import com.stegvis_api.stegvis_api.notes.repository.NoteCollectionRepository;
import com.stegvis_api.stegvis_api.notes.repository.NoteRepository;
import com.stegvis_api.stegvis_api.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class NoteCollectionService {

    private final NoteCollectionRepository noteCollectionRepository;
    private final NoteRepository noteRepository;
    private final UserService userService;
    private final NoteMapper noteMapper;

    @Transactional
    public AddNoteCollectionResponse createNoteCollection(AddNoteCollectionDTO noteCollectionDto, String userId) {
        userService.getUserByIdOrThrow(userId);

        NoteCollection noteCollection = noteMapper.toNoteCollection(noteCollectionDto);

        noteCollection.setUserId(userId);
        noteCollection.setNotes(new ArrayList<>());

        NoteCollection savedNoteCollection = noteCollectionRepository.save(noteCollection);
        log.info("NoteCollection created for userId={}, noteCollectionId={}", userId, savedNoteCollection.getId());
        return noteMapper.toAddNoteCollectionResponse(savedNoteCollection);
    }

    @Transactional
    public AddNoteToCollectionResponse addNoteToCollection(AddNoteToCollectionDTO dto, String userId) {
        userService.getUserByIdOrThrow(userId);

        Note note = noteRepository.findByIdAndUserId(dto.noteId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Note hittades inte för användare"));

        NoteCollection collection = noteCollectionRepository.findByIdAndUserId(dto.collectionId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Collection hittades inte för användare"));

        if (collection.getNotes() == null) {
            collection.setNotes(new ArrayList<>());
        }
        collection.addNote(note);
        noteCollectionRepository.save(collection);

        log.info("Note med id={} tillagd i collection={} för userId={}", dto.noteId(), dto.collectionId(), userId);

        return noteMapper.toAddNoteToCollectionResponse(collection, note.getId());
    }

    public NoteCollectionResponse getNoteCollectionWithNotes(String collectionId, String userId) {
        userService.getUserByIdOrThrow(userId);

        NoteCollection noteCollection = noteCollectionRepository.findByIdAndUserId(collectionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Collection hittades inte för userId=" + userId));

        return noteMapper.toNoteCollectionResponse(noteCollection);
    }

    public List<NoteCollectionResponse> getAllNoteCollections(String userId) {
        userService.getUserByIdOrThrow(userId);

        List<NoteCollection> noteCollections = noteCollectionRepository.findByUserId(userId);
        log.debug("Fetched {} notecollections for userId={}", noteCollections.size(), userId);

        return noteMapper.toNoteCollectionResponseList(noteCollections);
    }
}