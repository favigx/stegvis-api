package com.stegvis_api.stegvis_api.notes.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.exception.type.ResourceNotFoundException;
import com.stegvis_api.stegvis_api.notes.dto.AddNoteDTO;
import com.stegvis_api.stegvis_api.notes.dto.EditNoteDTO;
import com.stegvis_api.stegvis_api.notes.model.Note;
import com.stegvis_api.stegvis_api.repository.NoteRepository;
import com.stegvis_api.stegvis_api.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserService userService;

    public NoteService(NoteRepository noteRepository, UserService userService) {
        this.noteRepository = noteRepository;
        this.userService = userService;
    }

    public Note createNote(AddNoteDTO noteDto, String userId) {
        userService.getUserByIdOrThrow(userId);

        Note note = Note.builder()
                .userId(userId)
                .note(noteDto.getNote())
                .subject(noteDto.getSubject())
                .dateTime(Instant.now())
                .build();

        Note savedNote = noteRepository.save(note);
        log.info("Note created for userId={}, noteId={}", userId, savedNote.getId());
        return savedNote;
    }

    public Note editNote(String noteId, EditNoteDTO editDto, String userId) {
        userService.getUserByIdOrThrow(userId);

        Note note = noteRepository.findByIdAndUserId(noteId, userId)
                .orElseThrow(() -> {
                    log.warn("Edit failed: noteId={} not found for userId={}", noteId, userId);
                    return new ResourceNotFoundException("Note hittades inte för användare");
                });

        Note updatedNote = note.toBuilder()
                .note(editDto.getNote() != null ? editDto.getNote() : note.getNote())
                .subject(editDto.getSubject() != null ? editDto.getSubject() : note.getSubject())
                .dateTime(Instant.now())
                .build();

        Note savedNote = noteRepository.save(updatedNote);
        log.info("Note updated for userId={}, noteId={}", userId, savedNote.getId());
        return savedNote;
    }

    public Note getNoteById(String userId, String noteId) {
        userService.getUserByIdOrThrow(userId);

        return noteRepository.findByIdAndUserId(noteId, userId)
                .orElseThrow(() -> {
                    log.warn("Note not found: noteId={} for userId={}", noteId, userId);
                    return new ResourceNotFoundException("Note not found");
                });
    }

    public List<Note> getAllNotesForUser(String userId) {
        userService.getUserByIdOrThrow(userId);
        List<Note> notes = noteRepository.findByUserId(userId);
        log.debug("Fetched {} notes for userId={}", notes.size(), userId);
        return notes;
    }

    public Note deleteNoteById(String noteId, String userId) {
        userService.getUserByIdOrThrow(userId);

        Note note = noteRepository.findByIdAndUserId(noteId, userId)
                .orElseThrow(() -> {
                    log.warn("Delete failed: noteId={} not found for userId={}", noteId, userId);
                    return new ResourceNotFoundException("Note hittades inte för användare");
                });

        noteRepository.delete(note);
        log.info("Note deleted for userId={}, noteId={}", userId, noteId);
        return note;
    }

    public long countNotesByUserId(String userId) {
        userService.getUserByIdOrThrow(userId);

        long count = noteRepository.countByUserId(userId);
        log.debug("Counted {} notes for userId={}", count, userId);
        return count;
    }
}