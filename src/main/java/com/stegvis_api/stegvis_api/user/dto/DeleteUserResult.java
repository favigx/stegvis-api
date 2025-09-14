package com.stegvis_api.stegvis_api.user.dto;

public record DeleteUserResult(
        long deletedNotes,
        long deletedTodos,
        long deletedTasks) {
}