package com.stegvis_api.stegvis_api.todo.dto;

import java.time.Instant;

public record TodoFileResponse(
        String id,
        String fileName,
        String fileUrl,
        Instant dateTimeUploaded,
        String todoId) {
}
