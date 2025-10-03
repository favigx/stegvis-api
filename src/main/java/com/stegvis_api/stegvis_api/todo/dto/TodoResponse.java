package com.stegvis_api.stegvis_api.todo.dto;

public record TodoResponse(
        String id,
        String todo,
        String dateTimeCreated) {
}
