package com.stegvis_api.stegvis_api.notes.dto;

public record AddNoteToCollectionResponse(
        String id,
        String name,
        String noteId,
        int noteCount) {
}
