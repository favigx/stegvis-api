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
import com.stegvis_api.stegvis_api.notes.dto.NoteResponse;
import com.stegvis_api.stegvis_api.notes.dto.OptimizeNoteDTO;
import com.stegvis_api.stegvis_api.notes.dto.NoteFilterDTO;
import com.stegvis_api.stegvis_api.notes.service.NoteFilterService;
import com.stegvis_api.stegvis_api.notes.service.NoteService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notes")
public class NoteController {

        private final NoteService noteService;
        private final NoteFilterService noteFilterService;

        @PostMapping
        public ResponseEntity<AddNoteResponse> createNote(
                        @RequestBody AddNoteDTO addNoteDTO,
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {

                AddNoteResponse response = noteService.createNote(addNoteDTO, userPrincipal.getId());

                URI location = ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(response.id())
                                .toUri();

                return ResponseEntity.created(location).body(response);
        }

        @PutMapping("/{noteId}")
        public ResponseEntity<EditNoteResponse> editNote(
                        @PathVariable String noteId,
                        @RequestBody EditNoteDTO editNoteDTO,
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {

                EditNoteResponse response = noteService.editNote(noteId, editNoteDTO, noteId);

                return ResponseEntity.ok(response);
        }

        @GetMapping("/{noteId}")
        public ResponseEntity<NoteResponse> getNoteById(
                        @PathVariable String noteId,
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {

                NoteResponse response = noteService.getNoteById(userPrincipal.getId(), noteId);

                return ResponseEntity.ok(response);
        }

        @GetMapping
        public ResponseEntity<List<NoteResponse>> getUserNotes(
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {

                List<NoteResponse> dtoList = noteService.getAllNotesForUser(userPrincipal.getId());

                return ResponseEntity.ok(dtoList);
        }

        @GetMapping("/filter")
        public ResponseEntity<List<NoteResponse>> filterNotes(
                        @AuthenticationPrincipal UserPrincipal userPrincipal,
                        @RequestParam(required = false) String subject,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromDate,
                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toDate,
                        @RequestParam(defaultValue = "false") boolean ascending) {

                NoteFilterDTO filterDTO = new NoteFilterDTO(subject, fromDate, toDate, ascending);

                List<NoteResponse> dtoList = noteFilterService.filterNotes(userPrincipal.getId(), filterDTO);

                return ResponseEntity.ok(dtoList);
        }

        @DeleteMapping("/{noteId}")
        public ResponseEntity<DeleteNoteResponse> deleteNote(
                        @PathVariable String noteId,
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {

                DeleteNoteResponse response = noteService.deleteNoteById(noteId, userPrincipal.getId());

                return ResponseEntity.ok(response);
        }

        @GetMapping("/count")
        public ResponseEntity<Long> countUserNotes(@AuthenticationPrincipal UserPrincipal userPrincipal) {
                long count = noteService.countNotesByUserId(userPrincipal.getId());

                return ResponseEntity.ok(count);
        }

        @PutMapping("/{noteId}/optimize")
        public ResponseEntity<NoteResponse> optimizeNote(
                        @PathVariable String noteId,
                        @RequestBody OptimizeNoteDTO optimizeDto,
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {

                NoteResponse response = noteService.optimizeNoteWithAI(noteId, userPrincipal.getId(), optimizeDto);

                return ResponseEntity.ok(response);
        }
}