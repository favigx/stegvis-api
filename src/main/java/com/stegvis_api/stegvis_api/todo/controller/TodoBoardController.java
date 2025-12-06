package com.stegvis_api.stegvis_api.todo.controller;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

        @PutMapping("/{todoId}/notstarted")
        public ResponseEntity<Void> markTodoNotStarted(
                        @PathVariable String todoId,
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {
                todoBoardService.markTodoNotStarted(todoId, userPrincipal.getId());
                return ResponseEntity.noContent().build();
        }

        @PostMapping("/{todoId}/upload")
        public ResponseEntity<UploadFileResponse> uploadFile(
                        @PathVariable String todoId,
                        @RequestParam("file") MultipartFile file,
                        @AuthenticationPrincipal UserPrincipal userPrincipal) throws IOException {

                UploadFileDTO dto = new UploadFileDTO(todoId, file);
                UploadFileResponse response = todoBoardService.uploadFile(dto, userPrincipal.getId());

                URI location = ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/{fileName}")
                                .buildAndExpand(response.fileName())
                                .toUri();

                return ResponseEntity.created(location).body(response);
        }

        @GetMapping("/{todoFileId}/download")
        public ResponseEntity<String> getFileUrl(
                        @PathVariable String todoFileId,
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {

                String fileUrl = todoBoardService.getTodoFileUrl(todoFileId, userPrincipal.getId());

                return ResponseEntity.ok(fileUrl);
        }

        @GetMapping("/{todoId}/files")
        public ResponseEntity<List<TodoFileResponse>> getFilesForTodo(
                        @PathVariable String todoId,
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {

                List<TodoFileResponse> files = todoBoardService.getFilesForTodo(todoId, userPrincipal.getId());
                return ResponseEntity.ok(files);
        }

        @DeleteMapping("file/{fileId}")
        public ResponseEntity<Void> deleteTodoFile(
                        @PathVariable String fileId,
                        @AuthenticationPrincipal UserPrincipal userPrincipal) {

                todoBoardService.deleteTodoFile(fileId, userPrincipal.getId());
                return ResponseEntity.ok().build();
        }
}