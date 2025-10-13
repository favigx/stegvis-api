package com.stegvis_api.stegvis_api.goalplanner.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stegvis_api.stegvis_api.goalplanner.service.GoalPlannerService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/goalplanner")
public class GoalPlannerController {

    private final GoalPlannerService goalPlannerService;

    @GetMapping("/enums")
    public Map<String, Object> getAllEnums() {
        return goalPlannerService.getGradeEnums();
    }
}