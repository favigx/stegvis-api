package com.stegvis_api.stegvis_api.notes.dto;

public record EditNoteResponse(
        String id,
        String note,
        String subject,
        String dateTime) {
}
