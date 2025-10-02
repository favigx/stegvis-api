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
import org.springframework.transaction.annotation.Transactional;

import com.stegvis_api.stegvis_api.calender.deadline.dto.AddTaskDTO;
import com.stegvis_api.stegvis_api.calender.deadline.model.Task;
import com.stegvis_api.stegvis_api.calender.deadline.model.enums.Type;
import com.stegvis_api.stegvis_api.calender.deadline.repository.TaskRepository;
import com.stegvis_api.stegvis_api.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    @Transactional
    public Task addToCalender(AddTaskDTO addTaskDTO, String userId) {
        userService.getUserByIdOrThrow(userId);

        Instant instantDeadline = addTaskDTO.getDeadline().toInstant();

        Task task = Task.builder()
                .userId(userId)
                .subject(addTaskDTO.getSubject())
                .type(addTaskDTO.getType())
                .deadline(instantDeadline)
                .build();

        Task savedTask = taskRepository.save(task);
        log.info("Task added to calendar: taskId={}, userId={}", savedTask.getId(), userId);
        return savedTask;
    }

    public List<Task> getAllTasksForUser(String userId) {
        userService.getUserByIdOrThrow(userId);
        List<Task> tasks = taskRepository.findByUserId(userId);
        log.debug("Fetched {} tasks for userId={}", tasks.size(), userId);
        return tasks;
    }

    public Map<String, Object> getTypesEnum() {
        Map<String, Object> enums = new HashMap<>();
        enums.put("types", mapToTitleCase(Type.values()));
        log.debug("Fetched task types enum");
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
        long daysLeft = ChronoUnit.DAYS.between(now.toLocalDate(), deadlineZoned.toLocalDate());
        log.debug("Calculated days left={} for deadline={}", daysLeft, deadline);
        return daysLeft;
    }
}