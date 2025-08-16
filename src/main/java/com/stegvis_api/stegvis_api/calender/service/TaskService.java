package com.stegvis_api.stegvis_api.calender.service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.stegvis_api.stegvis_api.calender.dto.AddTaskDTO;
import com.stegvis_api.stegvis_api.calender.dto.TaskDTO;
import com.stegvis_api.stegvis_api.calender.model.Task;
import com.stegvis_api.stegvis_api.calender.model.enums.Type;
import com.stegvis_api.stegvis_api.exception.type.UserNotFoundException;
import com.stegvis_api.stegvis_api.user.model.User;
import com.stegvis_api.stegvis_api.user.service.UserService;

@Service
public class TaskService {

    private final MongoOperations mongoOperations;
    private final UserService userService;

    public TaskService(MongoOperations mongoOperations, UserService userService) {
        this.mongoOperations = mongoOperations;
        this.userService = userService;
    }

    public Task addToCalender(AddTaskDTO addTaskDTO, String userId) {

        User dBUser = userService.getUserById(userId);

        if (dBUser == null) {
            throw new UserNotFoundException("Användaren med id:" + userId + " hittades inte");
        }

        Instant instantDeadline = addTaskDTO.getDeadline().toInstant();

        Task task = Task.builder()
                .userId(userId)
                .subject(addTaskDTO.getSubject())
                .type(addTaskDTO.getType())
                .deadline(instantDeadline)
                .build();

        return mongoOperations.save(task);
    }

    public List<TaskDTO> getAllTasksForUser(String userId) {
        User dbUser = userService.getUserById(userId);

        if (dbUser == null) {
            throw new UserNotFoundException("Användaren med id: " + userId + " hittades inte");
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));

        List<Task> tasks = mongoOperations.find(query, Task.class);

        return tasks.stream()
                .map(task -> {
                    long daysLeft = calculateDaysLeft(task.getDeadline());
                    return TaskDTO.builder()
                            .id(task.getId())
                            .subject(task.getSubject())
                            .type(task.getType())
                            .deadline(task.getDeadline().toString())
                            .daysLeft(daysLeft)
                            .pastDue(daysLeft < 0)
                            .build();
                })
                .toList();

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
