package com.stegvis_api.stegvis_api.todo.dto;

public record TodoResponse(
                String id,
                String description,
                String subject,
                String status,
                String dateTimeCreated,
                String dateTimeCompleted) {
}