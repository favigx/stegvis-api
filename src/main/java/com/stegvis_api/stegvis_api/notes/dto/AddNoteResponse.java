package com.stegvis_api.stegvis_api.notes.dto;

public record AddNoteResponse(
        String id,
        String note,
        String subject,
        String dateTime) {
}
