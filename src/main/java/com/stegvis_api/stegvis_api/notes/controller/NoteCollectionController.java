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
import com.stegvis_api.stegvis_api.notes.dto.NoteCollectionDTO;
import com.stegvis_api.stegvis_api.notes.dto.NoteDTO;
import com.stegvis_api.stegvis_api.notes.model.NoteCollection;
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
        NoteCollection noteCollection = noteCollectionService.createNoteCollection(addNoteCollectionDTO,
                userPrincipal.getId());

        AddNoteCollectionResponse response = AddNoteCollectionResponse.builder()
                .id(noteCollection.getId())
                .name(noteCollection.getName())
                .build();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(noteCollection.getId()).toUri();

        return ResponseEntity.created(location).body(response);

    }

    @PostMapping("/add-note")
    public ResponseEntity<AddNoteToCollectionResponse> addNoteToCollection(
            @RequestBody AddNoteToCollectionDTO dto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        AddNoteToCollectionResponse response = noteCollectionService.addNoteToCollection(dto, userPrincipal.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{collectionId}")
    public ResponseEntity<NoteCollectionDTO> getNoteCollection(
            @PathVariable String collectionId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        NoteCollection collection = noteCollectionService.getNoteCollectionWithNotes(collectionId,
                userPrincipal.getId());

        List<NoteDTO> noteDTOs = collection.getNotes().stream()
                .map(note -> NoteDTO.builder()
                        .id(note.getId())
                        .note(note.getNote())
                        .subject(note.getSubject())
                        .dateTime(note.getDateTime().toString())
                        .build())
                .toList();

        NoteCollectionDTO dto = NoteCollectionDTO.builder()
                .id(collection.getId())
                .name(collection.getName())
                .notes(noteDTOs)
                .build();

        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<NoteCollectionDTO>> getAllNoteCollections(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        List<NoteCollection> collections = noteCollectionService.getAllNoteCollections(userPrincipal.getId());

        List<NoteCollectionDTO> dtos = collections.stream()
                .map(collection -> {
                    List<NoteDTO> noteDTOs = collection.getNotes().stream()
                            .map(note -> NoteDTO.builder()
                                    .id(note.getId())
                                    .note(note.getNote())
                                    .subject(note.getSubject())
                                    .dateTime(note.getDateTime().toString())
                                    .build())
                            .toList();

                    return NoteCollectionDTO.builder()
                            .id(collection.getId())
                            .name(collection.getName())
                            .notes(noteDTOs)
                            .build();
                })
                .toList();

        return ResponseEntity.ok(dtos);
    }
}