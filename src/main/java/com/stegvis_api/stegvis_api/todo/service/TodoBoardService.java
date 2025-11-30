package com.stegvis_api.stegvis_api.todo.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stegvis_api.stegvis_api.exception.type.ResourceNotFoundException;
import com.stegvis_api.stegvis_api.todo.dto.AddTodoDTO;
import com.stegvis_api.stegvis_api.todo.dto.AddTodoResponse;
import com.stegvis_api.stegvis_api.todo.dto.DeleteTodoResponse;
import com.stegvis_api.stegvis_api.todo.dto.TodoResponse;
import com.stegvis_api.stegvis_api.todo.enums.TodoStatus;
import com.stegvis_api.stegvis_api.todo.mapper.TodoBoardMapper;
import com.stegvis_api.stegvis_api.todo.model.TodoBoard;
import com.stegvis_api.stegvis_api.todo.repository.TodoBoardRepository;
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
}