package com.stegvis_api.stegvis_api.todo.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.stegvis_api.stegvis_api.config.security.UserPrincipal;
import com.stegvis_api.stegvis_api.todo.dto.*;
import com.stegvis_api.stegvis_api.todo.service.TodoBoardService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/todo")
public class TodoBoardController {

        private final TodoBoardService todoBoardService;

        @PostMapping
        public ResponseEntity<AddTodoResponse> createTodo(
                        @RequestBody AddTodoDTO addTodoDTO,
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {

                AddTodoResponse response = todoBoardService.createTodo(addTodoDTO, userPrincipal.getId());

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

                List<TodoResponse> dtoList = todoBoardService.getAllTodosForUser(userPrincipal.getId());

                return ResponseEntity.ok(dtoList);
        }

        @DeleteMapping("/{todoId}")
        public ResponseEntity<DeleteTodoResponse> deleteTodo(
                        @PathVariable String todoId,
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {

                DeleteTodoResponse response = todoBoardService.deleteTodoById(todoId, userPrincipal.getId());

                return ResponseEntity.ok(response);
        }

        @PutMapping("/{todoId}/ongoing")
        public ResponseEntity<Void> markTodoOngoing(
                        @PathVariable String todoId,
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {
                todoBoardService.markTodoOngoing(todoId, userPrincipal.getId());
                return ResponseEntity.noContent().build();
        }

        @PutMapping("/{todoId}/completed")
        public ResponseEntity<Void> markTodoCompleted(
                        @PathVariable String todoId,
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {
                todoBoardService.markTodoCompleted(todoId, userPrincipal.getId());
                return ResponseEntity.noContent().build();
        }
}