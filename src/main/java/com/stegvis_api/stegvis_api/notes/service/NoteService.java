package com.stegvis_api.stegvis_api.notes.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stegvis_api.stegvis_api.exception.type.ResourceNotFoundException;
import com.stegvis_api.stegvis_api.integration.openai.service.OpenAiNoteService;
import com.stegvis_api.stegvis_api.integration.skolverket.dto.SubjectCourseResponse;
import com.stegvis_api.stegvis_api.integration.skolverket.service.SkolverketService;
import com.stegvis_api.stegvis_api.notes.dto.AddNoteDTO;
import com.stegvis_api.stegvis_api.notes.dto.AddNoteResponse;
import com.stegvis_api.stegvis_api.notes.dto.DeleteNoteResponse;
import com.stegvis_api.stegvis_api.notes.dto.EditNoteDTO;
import com.stegvis_api.stegvis_api.notes.dto.EditNoteResponse;
import com.stegvis_api.stegvis_api.notes.dto.NoteResponse;
import com.stegvis_api.stegvis_api.notes.dto.OptimizeNoteDTO;
import com.stegvis_api.stegvis_api.notes.mapper.NoteMapper;
import com.stegvis_api.stegvis_api.notes.model.Note;
import com.stegvis_api.stegvis_api.notes.repository.NoteRepository;
import com.stegvis_api.stegvis_api.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserService userService;
    private final OpenAiNoteService openAiNoteService;
    private final SkolverketService skolverketService;
    private final NoteMapper noteMapper;

    @Transactional
    public AddNoteResponse createNote(AddNoteDTO noteDto, String userId) {
        userService.getUserByIdOrThrow(userId);

        Note note = noteMapper.toNote(noteDto);

        note.setUserId(userId);
        note.setDateTime(Instant.now());

        Note savedNote = noteRepository.save(note);
        log.info("Note created for userId={}, noteId={}", userId, savedNote.getId());
        return noteMapper.toAddResponse(savedNote);
    }

    public EditNoteResponse editNote(String noteId, EditNoteDTO editDto, String userId) {
        userService.getUserByIdOrThrow(userId);

        Note note = noteRepository.findByIdAndUserId(noteId, userId)
                .orElseThrow(() -> {
                    log.warn("Edit failed: noteId={} not found for userId={}", noteId, userId);
                    return new ResourceNotFoundException("Note hittades inte för användare");
                });

        noteMapper.updateNoteFromDto(editDto, note);

        note.setDateTime(Instant.now());

        Note savedNote = noteRepository.save(note);
        log.info("Note updated for userId={}, noteId={}", userId, savedNote.getId());
        return noteMapper.toEditNoteResponse(savedNote);
    }

    public NoteResponse getNoteById(String userId, String noteId) {
        userService.getUserByIdOrThrow(userId);

        Note note = noteRepository.findByIdAndUserId(noteId, userId)
                .orElseThrow(() -> {
                    log.warn("Note not found: noteId={} for userId={}", noteId, userId);
                    return new ResourceNotFoundException("Note not found");
                });
        return noteMapper.toNoteResponse(note);
    }

    public List<NoteResponse> getAllNotesForUser(String userId) {
        userService.getUserByIdOrThrow(userId);

        List<Note> notes = noteRepository.findByUserId(userId);
        log.debug("Fetched {} notes for userId={}", notes.size(), userId);
        return noteMapper.toNoteResponseList(notes);
    }

    @Transactional
    public DeleteNoteResponse deleteNoteById(String noteId, String userId) {
        userService.getUserByIdOrThrow(userId);

        Note note = noteRepository.findByIdAndUserId(noteId, userId)
                .orElseThrow(() -> {
                    log.warn("Delete failed: noteId={} not found for userId={}", noteId, userId);
                    return new ResourceNotFoundException("Note hittades inte för användare");
                });

        noteRepository.delete(note);
        log.info("Note deleted for userId={}, noteId={}", userId, noteId);
        return noteMapper.toDeleteNoteResponse(note);
    }

    public long countNotesByUserId(String userId) {
        userService.getUserByIdOrThrow(userId);

        long count = noteRepository.countByUserId(userId);
        log.debug("Counted {} notes for userId={}", count, userId);
        return count;
    }

    @Transactional
    public NoteResponse optimizeNoteWithAI(String noteId, String userId, OptimizeNoteDTO optimizeNoteDTO) {
        userService.getUserByIdOrThrow(userId);

        Note note = noteRepository.findByIdAndUserId(noteId, userId)
                .orElseThrow(() -> {
                    log.warn("Optimize failed: noteId={} not found for userId={}", noteId, userId);
                    return new ResourceNotFoundException("Note hittades inte för användare");
                });

        SubjectCourseResponse subjectCourse = skolverketService.getSubjectCourseDetails(optimizeNoteDTO.subjectCode(),
                optimizeNoteDTO.courseCode());

        String optimizedContent = openAiNoteService.generateOptimizedNote(note.getNote(), subjectCourse);

        Note updatedNote = noteMapper.toOptimizedNote(note, optimizedContent);

        Note savedNote = noteRepository.save(updatedNote);
        log.info("Note optimized with AI for userId={}, noteId={}", userId, noteId);
        return noteMapper.toNoteResponse(savedNote);
    }
}