package com.stegvis_api.stegvis_api.todo.service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.todo.enums.TodoStatus;
import com.stegvis_api.stegvis_api.todo.model.TodoBoard;
import com.stegvis_api.stegvis_api.todo.repository.TodoBoardRepository;

@Service
public class TodoDurationScheduler {

    private final TodoBoardRepository todoBoardRepository;

    public TodoDurationScheduler(TodoBoardRepository todoBoardRepository) {
        this.todoBoardRepository = todoBoardRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void incrementOngoingDuration() {
        List<TodoBoard> ongoingTodos = todoBoardRepository.findByStatus(TodoStatus.ONGOING);

        for (TodoBoard todo : ongoingTodos) {
            if (todo.getDateTimeCreated() != null) {
                long daysElapsed = Duration.between(todo.getDateTimeCreated(), Instant.now()).toDays();
                todo.setDurationDays((int) daysElapsed);

                todoBoardRepository.save(todo);
            }
        }
    }
}