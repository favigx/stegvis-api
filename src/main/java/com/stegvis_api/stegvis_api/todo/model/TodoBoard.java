package com.stegvis_api.stegvis_api.todo.model;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.stegvis_api.stegvis_api.todo.enums.TodoStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "userTodos")
public class TodoBoard {

    @Id
    private String id;

    private String userId;
    private String description;
    private String subject;

    @Builder.Default
    private TodoStatus status = TodoStatus.TODO;

    @Builder.Default
    private Instant dateTimeCreated = Instant.now();

    private Instant dateTimeCompleted;
    private int durationDays;
    private List<TodoFile> attachments;
    private List<String> links;
}