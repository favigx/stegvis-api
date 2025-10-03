package com.stegvis_api.stegvis_api.todo.dto;

public record DeleteTodoResponse(
        String id,
        String todo,
        String deletedAt,
        String message) {
}
