package com.stegvis_api.stegvis_api.notes.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.stegvis_api.stegvis_api.config.security.UserPrincipal;
import com.stegvis_api.stegvis_api.notes.dto.AddNoteCollectionDTO;
import com.stegvis_api.stegvis_api.notes.dto.AddNoteCollectionResponse;
import com.stegvis_api.stegvis_api.notes.dto.AddNoteToCollectionDTO;
import com.stegvis_api.stegvis_api.notes.dto.AddNoteToCollectionResponse;
import com.stegvis_api.stegvis_api.notes.dto.NoteCollectionResponse;
import com.stegvis_api.stegvis_api.notes.service.NoteCollectionService;

@RestController
@RequestMapping("/api/notes/notecollection")
public class NoteCollectionController {

        private final NoteCollectionService noteCollectionService;

        public NoteCollectionController(NoteCollectionService noteCollectionService) {
                this.noteCollectionService = noteCollectionService;
        }

        @PostMapping
        public ResponseEntity<AddNoteCollectionResponse> createNoteCollection(
                        @RequestBody AddNoteCollectionDTO addNoteCollectionDTO,
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {
                AddNoteCollectionResponse response = noteCollectionService.createNoteCollection(addNoteCollectionDTO,
                                userPrincipal.getId());

                URI location = ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(response.id())
                                .toUri();

                return ResponseEntity.created(location).body(response);

        }

        @PostMapping("/add-note")
        public ResponseEntity<AddNoteToCollectionResponse> addNoteToCollection(
                        @RequestBody AddNoteToCollectionDTO dto,
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {

                AddNoteToCollectionResponse response = noteCollectionService.addNoteToCollection(dto,
                                userPrincipal.getId());
                return ResponseEntity.ok(response);
        }

        @GetMapping("/{collectionId}")
        public ResponseEntity<NoteCollectionResponse> getNoteCollection(
                        @PathVariable String collectionId,
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {

                NoteCollectionResponse response = noteCollectionService
                                .getNoteCollectionWithNotes(collectionId, userPrincipal.getId());

                return ResponseEntity.ok(response);
        }

        @GetMapping
        public ResponseEntity<List<NoteCollectionResponse>> getAllNoteCollections(
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {

                List<NoteCollectionResponse> dtoList = noteCollectionService
                                .getAllNoteCollections(userPrincipal.getId());

                return ResponseEntity.ok(dtoList);
        }
}