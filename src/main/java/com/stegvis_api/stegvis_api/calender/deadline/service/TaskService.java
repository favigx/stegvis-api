package com.stegvis_api.stegvis_api.calender.deadline.service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.calender.deadline.dto.AddTaskDTO;
import com.stegvis_api.stegvis_api.calender.deadline.model.Task;
import com.stegvis_api.stegvis_api.calender.deadline.model.enums.Type;
import com.stegvis_api.stegvis_api.repository.TaskRepository;
import com.stegvis_api.stegvis_api.user.service.UserService;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public Task addToCalender(AddTaskDTO addTaskDTO, String userId) {
        userService.getUserByIdOrThrow(userId);

        Instant instantDeadline = addTaskDTO.getDeadline().toInstant();

        Task task = Task.builder()
                .userId(userId)
                .subject(addTaskDTO.getSubject())
                .type(addTaskDTO.getType())
                .deadline(instantDeadline)
                .build();

        return taskRepository.save(task);
    }

    public List<Task> getAllTasksForUser(String userId) {
        userService.getUserByIdOrThrow(userId);
        return taskRepository.findByUserId(userId);
    }

    public Map<String, Object> getTypesEnum() {
        Map<String, Object> enums = new HashMap<>();
        enums.put("types", mapToTitleCase(Type.values()));
        return enums;
    }

    private <E extends Enum<E>> List<String> mapToTitleCase(E[] enumValues) {
        return Arrays.stream(enumValues)
                .map(this::toTitleCase)
                .toList();
    }

    private <E extends Enum<E>> String toTitleCase(E e) {
        String s = e.name().toLowerCase();
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    public long calculateDaysLeft(Instant deadline) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Stockholm"));
        ZonedDateTime deadlineZoned = deadline.atZone(ZoneId.of("Europe/Stockholm"));
        return ChronoUnit.DAYS.between(now.toLocalDate(), deadlineZoned.toLocalDate());
    }
}
