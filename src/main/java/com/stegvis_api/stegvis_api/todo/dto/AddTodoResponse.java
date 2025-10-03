package com.stegvis_api.stegvis_api.todo.dto;

public record AddTodoResponse(
        String id,
        String todo,
        String dateTimeCreated) {
}
