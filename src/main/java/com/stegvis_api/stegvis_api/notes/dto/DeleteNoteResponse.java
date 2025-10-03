package com.stegvis_api.stegvis_api.notes.dto;

public record DeleteNoteResponse(
        String id,
        String note,
        String deletedAt,
        String message) {
}
