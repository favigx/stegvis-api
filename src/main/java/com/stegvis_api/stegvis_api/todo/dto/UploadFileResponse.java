package com.stegvis_api.stegvis_api.todo.dto;

import java.time.Instant;

public record UploadFileResponse(
        String id,
        String todoId,
        String fileName,
        String fileUrl,
        String fileType,
        long fileSize,
        Instant dateTimeUploaded) {
}