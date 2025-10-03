package com.stegvis_api.stegvis_api.notes.dto;

import java.time.Instant;

public record NoteFilterDTO(
        String subject,
        Instant fromDate,
        Instant toDate,
        boolean ascending) {
}