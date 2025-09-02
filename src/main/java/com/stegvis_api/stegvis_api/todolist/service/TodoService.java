package com.stegvis_api.stegvis_api.todolist.service;

import java.time.Instant;
import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.exception.type.TodoNotFoundException;
import com.stegvis_api.stegvis_api.todolist.dto.AddTodoDTO;
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

    public Todo createTodo(AddTodoDTO todoDto, String userId) {
        userService.getUserByIdOrThrow(userId);

        Todo todo = Todo.builder()
                .userId(userId)
                .todo(todoDto.getTodo())
                .dateTimeCreated(Instant.now())
                .build();

        return saveTodo(todo);
    }

    public List<Todo> getAllTodosForUser(String userId) {
        userService.getUserByIdOrThrow(userId);

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));

        return mongoOperations.find(query, Todo.class);
    }

    public Todo deleteTodoById(String todoId, String userId) {
        userService.getUserByIdOrThrow(userId);

        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(todoId)
                .and("userId").is(userId));

        Todo todo = mongoOperations.findOne(query, Todo.class);
        if (todo == null) {
            throw new TodoNotFoundException(
                    String.format("Todo med id %s hittades inte för användare %s", todoId, userId));
        }

        deleteTodo(todo);
        return todo;
    }

    public Todo saveTodo(Todo todo) {
        return mongoOperations.save(todo);
    }

    public void deleteTodo(Todo todo) {
        mongoOperations.remove(todo);
    }
}
