package com.stegvis_api.stegvis_api.notes.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class EditNoteResponse {
    private final String id;
    private final String note;
    private final String subject;
    private final String dateTime;
}
