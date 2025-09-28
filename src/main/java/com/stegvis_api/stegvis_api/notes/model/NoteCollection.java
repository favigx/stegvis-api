package com.stegvis_api.stegvis_api.notes.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.stegvis_api.stegvis_api.exception.type.ResourceAlreadyExistsException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "userNoteCollections")
public class NoteCollection {

    @Id
    private String id;

    private String userId;
    private String name;

    @Builder.Default
    private List<Note> notes = new ArrayList<>();

    public void addNote(Note note) {
        if (note == null) {
            throw new IllegalArgumentException("Note får inte vara null");
        }
        if (notes.stream().anyMatch(n -> n.getId().equals(note.getId()))) {
            throw new ResourceAlreadyExistsException("Note finns redan i collection");
        }
        notes.add(note);
    }
}
