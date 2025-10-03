package com.stegvis_api.stegvis_api.todo.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.stegvis_api.stegvis_api.config.security.UserPrincipal;
import com.stegvis_api.stegvis_api.todo.dto.*;
import com.stegvis_api.stegvis_api.todo.service.TodoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/todo")
public class TodoController {

        private final TodoService todoService;

        @PostMapping
        public ResponseEntity<AddTodoResponse> createTodo(
                        @RequestBody AddTodoDTO addTodoDTO,
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {

                AddTodoResponse response = todoService.createTodo(addTodoDTO, userPrincipal.getId());

                URI location = ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(response.id())
                                .toUri();

                return ResponseEntity.created(location).body(response);
        }

        @GetMapping
        public ResponseEntity<List<TodoResponse>> getMyTodos(
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {

                List<TodoResponse> dtoList = todoService.getAllTodosForUser(userPrincipal.getId());

                return ResponseEntity.ok(dtoList);
        }

        @DeleteMapping("/{todoId}")
        public ResponseEntity<DeleteTodoResponse> deleteTodo(
                        @PathVariable String todoId,
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {

                DeleteTodoResponse response = todoService.deleteTodoById(todoId, userPrincipal.getId());

                return ResponseEntity.ok(response);
        }
}