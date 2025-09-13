package com.stegvis_api.stegvis_api.todolist.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.exception.type.ResourceNotFoundException;
import com.stegvis_api.stegvis_api.todolist.dto.AddTodoDTO;
import com.stegvis_api.stegvis_api.todolist.model.Todo;
import com.stegvis_api.stegvis_api.repository.TodoRepository;
import com.stegvis_api.stegvis_api.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserService userService;

    public TodoService(TodoRepository todoRepository, UserService userService) {
        this.todoRepository = todoRepository;
        this.userService = userService;
    }

    public Todo createTodo(AddTodoDTO todoDto, String userId) {
        userService.getUserByIdOrThrow(userId);

        Todo todo = Todo.builder()
                .userId(userId)
                .todo(todoDto.getTodo())
                .dateTimeCreated(Instant.now())
                .build();

        Todo savedTodo = todoRepository.save(todo);
        log.debug("Created todo: id={} for user id={}", savedTodo.getId(), userId);
        return savedTodo;
    }

    public List<Todo> getAllTodosForUser(String userId) {
        userService.getUserByIdOrThrow(userId);
        List<Todo> todos = todoRepository.findByUserId(userId);
        log.debug("Fetched {} todos for user id={}", todos.size(), userId);
        return todos;
    }

    public Todo deleteTodoById(String todoId, String userId) {
        userService.getUserByIdOrThrow(userId);

        Todo todo = todoRepository.findByIdAndUserId(todoId, userId)
                .orElseThrow(() -> {
                    log.warn("Todo not found: id={} for user id={}", todoId, userId);
                    return new ResourceNotFoundException(
                            String.format("Todo med id %s hittades inte för användare %s", todoId, userId));
                });

        todoRepository.delete(todo);
        log.debug("Deleted todo: id={} for user id={}", todoId, userId);
        return todo;
    }
}