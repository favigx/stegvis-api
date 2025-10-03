package com.stegvis_api.stegvis_api.todo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.stegvis_api.stegvis_api.todo.model.Todo;

public interface TodoRepository extends MongoRepository<Todo, String> {
    List<Todo> findByUserId(String userId);

    Optional<Todo> findByIdAndUserId(String id, String userId);

    long deleteByUserId(String userId);
}
