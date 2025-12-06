package com.stegvis_api.stegvis_api.todo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.stegvis_api.stegvis_api.todo.model.TodoFile;

public interface TodoFileRepository extends MongoRepository<TodoFile, String> {
    List<TodoFile> findByTodoId(String todoId);
}