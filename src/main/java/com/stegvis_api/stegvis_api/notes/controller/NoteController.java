package com.stegvis_api.stegvis_api.notes.controller;

import java.net.URI;
import java.time.Instant;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.stegvis_api.stegvis_api.config.security.UserPrincipal;
import com.stegvis_api.stegvis_api.notes.dto.AddNoteDTO;
import com.stegvis_api.stegvis_api.notes.dto.AddNoteResponse;
import com.stegvis_api.stegvis_api.notes.dto.DeleteNoteResponse;
import com.stegvis_api.stegvis_api.notes.dto.EditNoteDTO;
import com.stegvis_api.stegvis_api.notes.dto.EditNoteResponse;
import com.stegvis_api.stegvis_api.notes.dto.NoteDTO;
import com.stegvis_api.stegvis_api.notes.dto.NoteFilterDTO;
import com.stegvis_api.stegvis_api.notes.model.Note;
import com.stegvis_api.stegvis_api.notes.service.NoteFilterService;
import com.stegvis_api.stegvis_api.notes.service.NoteService;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

        private final NoteService noteService;
        private final NoteFilterService noteFilterService;

        public NoteController(NoteService noteService, NoteFilterService noteFilterService) {
                this.noteService = noteService;
                this.noteFilterService = noteFilterService;
        }

        @PostMapping
        public ResponseEntity<AddNoteResponse> createNote(
                        @RequestBody AddNoteDTO addNoteDTO,
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {

                Note note = noteService.createNote(addNoteDTO, userPrincipal.getId());

                AddNoteResponse response = AddNoteResponse.builder()
                                .id(note.getId())
                                .note(note.getNote())
                                .subject(note.getSubject())
                                .dateTime(note.getDateTime().toString())
                                .build();

                URI location = ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(note.getId())
                                .toUri();

                return ResponseEntity.created(location).body(response);
        }

        @PutMapping("/{noteId}")
        public ResponseEntity<EditNoteResponse> editNote(
                        @PathVariable String noteId,
                        @RequestBody EditNoteDTO editNoteDTO,
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {

                Note updatedNote = noteService.editNote(noteId, editNoteDTO, userPrincipal.getId());

                EditNoteResponse response = EditNoteResponse.builder()
                                .id(updatedNote.getId())
                                .note(updatedNote.getNote())
                                .subject(updatedNote.getSubject())
                                .dateTime(updatedNote.getDateTime().toString())
                                .build();

                return ResponseEntity.ok(response);
        }

        @GetMapping("/{noteId}")
        public ResponseEntity<NoteDTO> getNoteById(
                        @PathVariable String noteId,
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {

                Note note = noteService.getNoteById(userPrincipal.getId(), noteId);

                NoteDTO response = NoteDTO.builder()
                                .id(note.getId())
                                .note(note.getNote())
                                .subject(note.getSubject())
                                .dateTime(note.getDateTime().toString())
                                .build();

                return ResponseEntity.ok(response);
        }

        @GetMapping
        public ResponseEntity<List<NoteDTO>> getUserNotes(
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {

                List<Note> notes = noteService.getAllNotesForUser(userPrincipal.getId());

                List<NoteDTO> dtoList = notes.stream()
                                .map(note -> NoteDTO.builder()
                                                .id(note.getId())
                                                .note(note.getNote())
                                                .subject(note.getSubject())
                                                .dateTime(note.getDateTime().toString())
                                                .build())
                                .toList();

                return ResponseEntity.ok(dtoList);
        }

        @GetMapping("/filter")
        public ResponseEntity<List<NoteDTO>> filterNotes(
                        @AuthenticationPrincipal UserPrincipal userPrincipal,
                        @RequestParam(required = false) String subject,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromDate,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toDate,
                        @RequestParam(defaultValue = "false") boolean ascending) {

                NoteFilterDTO filterDTO = new NoteFilterDTO(subject, fromDate, toDate, ascending);
                List<Note> notes = noteFilterService.filterNotes(userPrincipal.getId(), filterDTO);

                List<NoteDTO> dtoList = notes.stream()
                                .map(note -> NoteDTO.builder()
                                                .id(note.getId())
                                                .note(note.getNote())
                                                .subject(note.getSubject())
                                                .dateTime(note.getDateTime().toString())
                                                .build())
                                .toList();

                return ResponseEntity.ok(dtoList);
        }

        @DeleteMapping("/{noteId}")
        public ResponseEntity<DeleteNoteResponse> deleteNote(
                        @PathVariable String noteId,
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {

                Note deletedNote = noteService.deleteNoteById(noteId, userPrincipal.getId());

                DeleteNoteResponse response = DeleteNoteResponse.builder()
                                .id(deletedNote.getId())
                                .note(deletedNote.getNote())
                                .deletedAt(Instant.now().toString())
                                .message("Note har raderats framgångsrikt.")
                                .build();

                return ResponseEntity.ok(response);
        }

        @GetMapping("/count")
        public ResponseEntity<Long> countUserNotes(@AuthenticationPrincipal UserPrincipal userPrincipal) {
                long count = noteService.countNotesByUserId(userPrincipal.getId());

                return ResponseEntity.ok(count);
        }

        @PutMapping("/{noteId}/optimize/{subjectCode}/{courseCode}")
        public ResponseEntity<NoteDTO> optimizeNote(
                        @PathVariable String noteId, @PathVariable String subjectCode, @PathVariable String courseCode,
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {

                Note optimizedNote = noteService.optimizeNoteWithAI(noteId, userPrincipal.getId(), subjectCode,
                                courseCode);

                NoteDTO response = NoteDTO.builder()
                                .id(optimizedNote.getId())
                                .note(optimizedNote.getNote())
                                .subject(optimizedNote.getSubject())
                                .dateTime(optimizedNote.getDateTime().toString())
                                .build();

                return ResponseEntity.ok(response);
        }
}