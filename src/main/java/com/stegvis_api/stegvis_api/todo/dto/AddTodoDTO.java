package com.stegvis_api.stegvis_api.todo.dto;

import java.util.List;

public record AddTodoDTO(
        String description,
        String subject,
        List<String> links) {
}