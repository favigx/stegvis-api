package com.stegvis_api.stegvis_api.calender.deadline.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.stegvis_api.stegvis_api.calender.deadline.dto.AddTaskDTO;
import com.stegvis_api.stegvis_api.calender.deadline.dto.AddTaskResponse;
import com.stegvis_api.stegvis_api.calender.deadline.dto.TaskDTO;
import com.stegvis_api.stegvis_api.calender.deadline.model.Task;
import com.stegvis_api.stegvis_api.calender.deadline.service.TaskService;
import com.stegvis_api.stegvis_api.config.security.UserPrincipal;

@RestController
@RequestMapping("/api/calender/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<AddTaskResponse> addTaskToCalender(
            @RequestBody AddTaskDTO addTaskDTO,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Task task = taskService.addToCalender(addTaskDTO, userPrincipal.getId());

        long daysLeft = taskService.calculateDaysLeft(task.getDeadline());

        AddTaskResponse response = AddTaskResponse.builder()
                .taskId(task.getId())
                .subject(task.getSubject())
                .type(task.getType())
                .deadline(task.getDeadline().toString())
                .daysLeft(daysLeft)
                .pastDue(daysLeft < 0)
                .build();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(task.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getTasks(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<Task> tasks = taskService.getAllTasksForUser(userPrincipal.getId());

        List<TaskDTO> dtoList = tasks.stream()
                .map(task -> TaskDTO.builder()
                        .id(task.getId())
                        .subject(task.getSubject())
                        .type(task.getType())
                        .deadline(task.getDeadline().toString())
                        .daysLeft(taskService.calculateDaysLeft(task.getDeadline()))
                        .pastDue(taskService.calculateDaysLeft(task.getDeadline()) < 0)
                        .build())
                .toList();

        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/enum")
    public Map<String, Object> getAllEnums() {
        return taskService.getTypesEnum();
    }
}