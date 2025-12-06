package com.stegvis_api.stegvis_api.todo.dto;

import org.springframework.web.multipart.MultipartFile;

public record UploadFileDTO(
        String todoId,
        MultipartFile file) {
}