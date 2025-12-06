package com.stegvis_api.stegvis_api.todo.service;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.stegvis_api.stegvis_api.exception.type.ResourceNotFoundException;
import com.stegvis_api.stegvis_api.integration.blackblaze.service.BlackblazeService;
import com.stegvis_api.stegvis_api.todo.dto.AddTodoDTO;
import com.stegvis_api.stegvis_api.todo.dto.AddTodoResponse;
import com.stegvis_api.stegvis_api.todo.dto.DeleteTodoResponse;
import com.stegvis_api.stegvis_api.todo.dto.TodoFileResponse;
import com.stegvis_api.stegvis_api.todo.dto.TodoResponse;
import com.stegvis_api.stegvis_api.todo.dto.UploadFileDTO;
import com.stegvis_api.stegvis_api.todo.dto.UploadFileResponse;
import com.stegvis_api.stegvis_api.todo.enums.TodoStatus;
import com.stegvis_api.stegvis_api.todo.mapper.TodoBoardMapper;
import com.stegvis_api.stegvis_api.todo.model.TodoBoard;
import com.stegvis_api.stegvis_api.todo.model.TodoFile;
import com.stegvis_api.stegvis_api.todo.repository.TodoBoardRepository;
import com.stegvis_api.stegvis_api.todo.repository.TodoFileRepository;
import com.stegvis_api.stegvis_api.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class TodoBoardService {

    private final TodoBoardRepository todoBoardRepository;
    private final UserService userService;
    private final TodoBoardMapper todoBoardMapper;
    private final BlackblazeService backblazeService;
    private final TodoFileRepository todoFileRepository;

    @Transactional
    public AddTodoResponse createTodo(AddTodoDTO todoDto, String userId) {
        userService.getUserByIdOrThrow(userId);

        TodoBoard todo = todoBoardMapper.toTodo(todoDto);

        todo.setUserId(userId);

        TodoBoard savedTodo = todoBoardRepository.save(todo);
        log.debug("Created todo: id={} for user id={}", savedTodo.getId(), userId);
        return todoBoardMapper.toAddTodoResponse(savedTodo);
    }

    public List<TodoResponse> getAllTodosForUser(String userId) {
        userService.getUserByIdOrThrow(userId);
        List<TodoBoard> todos = todoBoardRepository.findByUserId(userId);
        log.debug("Fetched {} todos for user id={}", todos.size(), userId);
        return todoBoardMapper.toTodoResponseList(todos);
    }

    public DeleteTodoResponse deleteTodoById(String todoId, String userId) {
        userService.getUserByIdOrThrow(userId);

        TodoBoard todo = todoBoardRepository.findByIdAndUserId(todoId, userId)
                .orElseThrow(() -> {
                    log.warn("Todo not found: id={} for user id={}", todoId, userId);
                    return new ResourceNotFoundException(
                            String.format("Todo med id %s hittades inte för användare %s", todoId, userId));
                });

        if (todo.getAttachments() != null) {
            todo.getAttachments().forEach(file -> {
                try {
                    backblazeService.deleteFile(file.getFileName());
                    log.debug("Deleted file: {}", file.getFileName());
                } catch (Exception e) {
                    log.error("Kunde inte radera fil: {}", file.getFileName(), e);
                }
            });
        }

        todoBoardRepository.delete(todo);
        log.debug("Deleted todo: id={} for user id={}", todoId, userId);

        return todoBoardMapper.toDeleteTodoResponse(todo);
    }

    public void markTodoOngoing(String todoId, String userId) {
        userService.getUserByIdOrThrow(userId);

        TodoBoard todo = todoBoardRepository.findByIdAndUserId(todoId, userId)
                .orElseThrow(() -> {
                    log.warn("Todo not found: id={} for user id={}", todoId, userId);
                    return new ResourceNotFoundException(
                            String.format("Todo med id %s hittades inte för användare %s", todoId, userId));
                });

        todo.setStatus(TodoStatus.ONGOING);
        todo.setDurationDays(0);
        todoBoardRepository.save(todo);
        log.debug("Marked todo as ongoing: id={} for user id={}", todoId, userId);
    }

    public void markTodoCompleted(String todoId, String userId) {
        userService.getUserByIdOrThrow(userId);

        TodoBoard todo = todoBoardRepository.findByIdAndUserId(todoId, userId)
                .orElseThrow(() -> {
                    log.warn("Todo not found: id={} for user id={}", todoId, userId);
                    return new ResourceNotFoundException(
                            String.format("Todo med id %s hittades inte för användare %s", todoId, userId));
                });

        todo.setStatus(TodoStatus.COMPLETED);
        todo.setDateTimeCompleted(Instant.now());
        todoBoardRepository.save(todo);
        log.debug("Marked todo as completed: id={} for user id={}", todoId, userId);
    }

    public void markTodoNotStarted(String todoId, String userId) {
        userService.getUserByIdOrThrow(userId);

        TodoBoard todo = todoBoardRepository.findByIdAndUserId(todoId, userId)
                .orElseThrow(() -> {
                    log.warn("Todo not found: id={} for user id={}", todoId, userId);
                    return new ResourceNotFoundException(
                            String.format("Todo med id %s hittades inte för användare %s", todoId, userId));
                });

        todo.setStatus(TodoStatus.TODO);
        todoBoardRepository.save(todo);
        log.debug("Marked todo as not started: id={} for user id={}", todoId, userId);
    }

    @Transactional
    public UploadFileResponse uploadFile(UploadFileDTO dto, String userId) throws IOException {
        TodoBoard todo = todoBoardRepository.findByIdAndUserId(dto.todoId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Todo med id %s hittades inte för användare %s", dto.todoId(), userId)));

        MultipartFile file = dto.file();

        String uploadedFileUrl = backblazeService.uploadFile(file.getOriginalFilename(), file.getResource());

        TodoFile todoFile = todoBoardMapper.toTodoFile(dto);
        todoFile.setFileUrl(uploadedFileUrl);
        todoFile.setDateTimeUploaded(Instant.now());
        todoFile.setTodoId(todo.getId());

        TodoFile savedFile = todoFileRepository.save(todoFile);
        log.debug("Uppladdad fil '{}' för todoId={} av userId={}", file.getOriginalFilename(), todo.getId(), userId);

        return todoBoardMapper.toTodoFileResponse(savedFile);
    }

    public String getTodoFileUrl(String todoFileId, String userId) {
        TodoFile todoFile = todoFileRepository.findById(todoFileId)
                .orElseThrow(() -> new ResourceNotFoundException("Filen hittades inte"));

        todoBoardRepository.findByIdAndUserId(todoFile.getTodoId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Todo med id %s hittades inte för användare %s", todoFile.getTodoId(), userId)));

        return backblazeService.getFileUrl(todoFile.getFileName(), 3600);
    }

    @Transactional(readOnly = true)
    public List<TodoFileResponse> getFilesForTodo(String todoId, String userId) {
        TodoBoard todo = todoBoardRepository.findByIdAndUserId(todoId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Todo med id %s hittades inte för användare %s", todoId, userId)));

        List<TodoFile> files = todoFileRepository.findByTodoId(todo.getId());

        return files.stream()
                .map(file -> new TodoFileResponse(
                        file.getId(),
                        file.getFileName(),
                        file.getFileUrl(),
                        file.getDateTimeUploaded(),
                        file.getTodoId()))
                .toList();
    }

    @Transactional
    public void deleteTodoFile(String todoFileId, String userId) {
        TodoFile todoFile = todoFileRepository.findById(todoFileId)
                .orElseThrow(() -> new ResourceNotFoundException("Filen hittades inte"));

        todoBoardRepository.findByIdAndUserId(todoFile.getTodoId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Todo med id %s hittades inte för användare %s", todoFile.getTodoId(), userId)));

        try {
            backblazeService.deleteFile(todoFile.getFileName());
            log.debug("Deleted file from Backblaze: {}", todoFile.getFileName());
        } catch (Exception e) {
            log.error("Kunde inte radera fil från Backblaze: {}", todoFile.getFileName(), e);
            throw new RuntimeException("Kunde inte radera fil från servern");
        }

        todoFileRepository.delete(todoFile);
        log.debug("Deleted file from database: {}", todoFile.getFileName());
    }

}