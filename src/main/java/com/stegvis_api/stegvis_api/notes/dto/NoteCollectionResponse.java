package com.stegvis_api.stegvis_api.notes.dto;

import java.util.List;

public record NoteCollectionResponse(
                String id,
                String name,
                List<NoteResponse> notes) {
}
