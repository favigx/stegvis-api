package com.stegvis_api.stegvis_api.todolist.controller;

import java.net.URI;
import java.time.Instant;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.stegvis_api.stegvis_api.config.security.UserPrincipal;
import com.stegvis_api.stegvis_api.todolist.dto.*;
import com.stegvis_api.stegvis_api.todolist.model.Todo;
import com.stegvis_api.stegvis_api.todolist.service.TodoService;

@RestController
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    public ResponseEntity<AddTodoResponse> createTodo(
            @RequestBody AddTodoDTO addTodoDTO,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Todo todo = todoService.createTodo(addTodoDTO, userPrincipal.getId());

        AddTodoResponse response = AddTodoResponse.builder()
                .id(todo.getId())
                .todo(todo.getTodo())
                .dateTimeCreated(todo.getDateTimeCreated().toString())
                .build();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(todo.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TodoDTO>> getMyTodos(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        List<Todo> todos = todoService.getAllTodosForUser(userPrincipal.getId());

        List<TodoDTO> dtoList = todos.stream()
                .map(todo -> TodoDTO.builder()
                        .id(todo.getId())
                        .todo(todo.getTodo())
                        .dateTimeCreated(todo.getDateTimeCreated().toString())
                        .build())
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<DeleteTodoResponse> deleteTodo(
            @PathVariable String todoId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Todo deletedTodo = todoService.deleteTodoById(todoId, userPrincipal.getId());

        DeleteTodoResponse response = DeleteTodoResponse.builder()
                .id(deletedTodo.getId())
                .todo(deletedTodo.getTodo())
                .deletedAt(Instant.now().toString())
                .message("Todo har raderats framgångsrikt.")
                .build();

        return ResponseEntity.ok(response);
    }
}
