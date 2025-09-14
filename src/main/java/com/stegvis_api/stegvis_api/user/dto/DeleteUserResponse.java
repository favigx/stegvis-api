package com.stegvis_api.stegvis_api.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class DeleteUserResponse {

    private final String id;
    private final long deletedNotes;
    private final long deletedTasks;
    private final long deletedTodos;
    private final String deletedAt;
    private final String status;
    private final String message;
}
