package com.stegvis_api.stegvis_api.todo.dto;

import java.util.List;

public record TodoResponse(
        String id,
        String description,
        String subject,
        List<String> links,
        String status,
        String dateTimeCreated,
        String dateTimeCompleted) {
}