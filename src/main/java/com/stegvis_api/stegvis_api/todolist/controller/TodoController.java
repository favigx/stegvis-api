package com.stegvis_api.stegvis_api.todolist.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.stegvis_api.stegvis_api.calender.deadline.dto.TaskDTO;
import com.stegvis_api.stegvis_api.config.security.UserPrincipal;
import com.stegvis_api.stegvis_api.todolist.dto.AddTodoDTO;
import com.stegvis_api.stegvis_api.todolist.dto.AddTodoResponse;
import com.stegvis_api.stegvis_api.todolist.dto.TodoDTO;
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

    @PreAuthorize("#userId == principal.id")
    @GetMapping("/{userId}")
    public ResponseEntity<List<TodoDTO>> getTasksForUser(@PathVariable String userId) {
        List<TodoDTO> tasks = todoService.getAllTodosForUser(userId);
        return ResponseEntity.ok(tasks);
    }

}
