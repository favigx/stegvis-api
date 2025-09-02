package com.stegvis_api.stegvis_api.todolist.service;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.calender.deadline.dto.TaskDTO;
import com.stegvis_api.stegvis_api.calender.deadline.model.Task;
import com.stegvis_api.stegvis_api.todolist.dto.AddTodoDTO;
import com.stegvis_api.stegvis_api.todolist.dto.TodoDTO;
import com.stegvis_api.stegvis_api.todolist.model.Todo;
import com.stegvis_api.stegvis_api.user.service.UserService;

@Service
public class TodoService {

    private final MongoOperations mongoOperations;
    private final UserService userService;

    public TodoService(MongoOperations mongoOperations, UserService userService) {
        this.mongoOperations = mongoOperations;
        this.userService = userService;
    }

    public Todo saveTodo(Todo todo) {
        return mongoOperations.save(todo);
    }

    public Todo createTodo(AddTodoDTO todoDto, String userId) {

        userService.getUserById(userId);

        Todo todo = Todo.builder()
                .userId(userId)
                .todo(todoDto.getTodo())
                .dateTimeCreated(Instant.now())
                .build();

        return saveTodo(todo);
    }

    public List<TodoDTO> getAllTodosForUser(String userId) {

        userService.getUserById(userId);

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));

        List<Todo> todos = mongoOperations.find(query, Todo.class);

        return todos.stream()
                .map(todo -> {
                    return TodoDTO.builder()
                            .id(todo.getId())
                            .todo(todo.getTodo())
                            .dateTimeCreated(todo.getDateTimeCreated().toString())
                            .build();
                })
                .toList();

    }

}
