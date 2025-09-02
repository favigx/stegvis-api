package com.stegvis_api.stegvis_api.todolist.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class DeleteTodoResponse {
    private final String id;
    private final String todo;
    private final String deletedAt;
    private final String message;
}
