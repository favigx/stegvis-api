package com.stegvis_api.stegvis_api.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.stegvis_api.stegvis_api.calender.deadline.model.Task;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByUserId(String userId);
}
