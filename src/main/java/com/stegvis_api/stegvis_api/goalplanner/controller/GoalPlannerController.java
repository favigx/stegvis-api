package com.stegvis_api.stegvis_api.goalplanner.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stegvis_api.stegvis_api.config.security.UserPrincipal;
import com.stegvis_api.stegvis_api.goalplanner.dto.MeritValueResponse;
import com.stegvis_api.stegvis_api.goalplanner.service.GoalPlannerService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/goalplanner")
public class GoalPlannerController {

    private final GoalPlannerService goalPlannerService;

    // @GetMapping("/merit-value")
    // public ResponseEntity<MeritValueResponse>
    // getMeritValue(@AuthenticationPrincipal UserPrincipal userPrincipal) {
    // double meritValue =
    // goalPlannerService.calculateMeriteValueForUser(userPrincipal.getId());

    // MeritValueResponse response = MeritValueResponse.builder()
    // .userId(userPrincipal.getId())
    // .meritValue(meritValue)
    // .build();

    // return ResponseEntity.ok(response);
    // }

    @GetMapping("/enums")
    public Map<String, Object> getAllEnums() {
        return goalPlannerService.getGradeEnums();
    }
}