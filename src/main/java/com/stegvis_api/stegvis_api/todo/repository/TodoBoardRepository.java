package com.stegvis_api.stegvis_api.todo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.stegvis_api.stegvis_api.todo.model.TodoBoard;

public interface TodoBoardRepository extends MongoRepository<TodoBoard, String> {
    List<TodoBoard> findByUserId(String userId);

    Optional<TodoBoard> findByIdAndUserId(String id, String userId);

    long deleteByUserId(String userId);
}