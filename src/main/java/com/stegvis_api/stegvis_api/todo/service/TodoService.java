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
import com.stegvis_api.stegvis_api.todo.mapper.TodoMapper;
import com.stegvis_api.stegvis_api.todo.model.Todo;
import com.stegvis_api.stegvis_api.todo.repository.TodoRepository;
import com.stegvis_api.stegvis_api.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserService userService;
    private final TodoMapper todoMapper;

    @Transactional
    public AddTodoResponse createTodo(AddTodoDTO todoDto, String userId) {
        userService.getUserByIdOrThrow(userId);

        Todo todo = todoMapper.toTodo(todoDto);

        todo.setUserId(userId);
        todo.setDateTimeCreated(Instant.now());

        Todo savedTodo = todoRepository.save(todo);
        log.debug("Created todo: id={} for user id={}", savedTodo.getId(), userId);
        return todoMapper.toAddTodoResponse(savedTodo);
    }

    public List<TodoResponse> getAllTodosForUser(String userId) {
        userService.getUserByIdOrThrow(userId);
        List<Todo> todos = todoRepository.findByUserId(userId);
        log.debug("Fetched {} todos for user id={}", todos.size(), userId);
        return todoMapper.toTodoResponseList(todos);
    }

    public DeleteTodoResponse deleteTodoById(String todoId, String userId) {
        userService.getUserByIdOrThrow(userId);

        Todo todo = todoRepository.findByIdAndUserId(todoId, userId)
                .orElseThrow(() -> {
                    log.warn("Todo not found: id={} for user id={}", todoId, userId);
                    return new ResourceNotFoundException(
                            String.format("Todo med id %s hittades inte för användare %s", todoId, userId));
                });

        todoRepository.delete(todo);
        log.debug("Deleted todo: id={} for user id={}", todoId, userId);
        return todoMapper.toDeleteTodoResponse(todo);
    }
}