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

        return noteRepository.save(note);
    }

    public Note editNote(String noteId, EditNoteDTO editDto, String userId) {
        userService.getUserByIdOrThrow(userId);

        Note note = noteRepository.findByIdAndUserId(noteId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ("Note hittades inte för användare")));

        Note updatedNote = note.toBuilder()
                .note(editDto.getNote() != null ? editDto.getNote() : note.getNote())
                .subject(editDto.getSubject() != null ? editDto.getSubject() : note.getSubject())
                .dateTime(Instant.now())
                .build();

        return noteRepository.save(updatedNote);
    }

    public Note getNoteById(String userId, String noteId) {
        userService.getUserByIdOrThrow(userId);
        return noteRepository.findByIdAndUserId(noteId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
    }

    public List<Note> getAllNotesForUser(String userId) {
        userService.getUserByIdOrThrow(userId);
        return noteRepository.findByUserId(userId);
    }

    public Note deleteNoteById(String noteId, String userId) {
        userService.getUserByIdOrThrow(userId);

        Note note = noteRepository.findByIdAndUserId(noteId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ("Note hittades inte för användare")));

        noteRepository.delete(note);
        return note;
    }

    public long countNotesByUserId(String userId) {
        userService.getUserByIdOrThrow(userId);

        return noteRepository.countByUserId(userId);
    }
}