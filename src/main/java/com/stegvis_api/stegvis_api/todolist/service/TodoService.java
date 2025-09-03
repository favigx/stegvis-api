package com.stegvis_api.stegvis_api.todolist.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.exception.type.ResourceNotFoundException;
import com.stegvis_api.stegvis_api.todolist.dto.AddTodoDTO;
import com.stegvis_api.stegvis_api.todolist.model.Todo;
import com.stegvis_api.stegvis_api.repository.TodoRepository;
import com.stegvis_api.stegvis_api.user.service.UserService;

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

        return todoRepository.save(todo);
    }

    public List<Todo> getAllTodosForUser(String userId) {
        userService.getUserByIdOrThrow(userId);
        return todoRepository.findByUserId(userId);
    }

    public Todo deleteTodoById(String todoId, String userId) {
        userService.getUserByIdOrThrow(userId);

        Todo todo = todoRepository.findByIdAndUserId(todoId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Todo med id %s hittades inte för användare %s", todoId, userId)));

        todoRepository.delete(todo);
        return todo;
    }
}
