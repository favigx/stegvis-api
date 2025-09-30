package com.stegvis_api.stegvis_api.integration.uhr.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stegvis_api.stegvis_api.config.security.UserPrincipal;
import com.stegvis_api.stegvis_api.integration.uhr.dto.EligibleProgramResponse;
import com.stegvis_api.stegvis_api.integration.uhr.service.UHRService;

@RestController
@RequestMapping("/api/uhr")
public class UHRController {

    private final UHRService uhrService;

    public UHRController(UHRService uhrService) {
        this.uhrService = uhrService;
    }

    @GetMapping("/eligible-programs")
    public ResponseEntity<List<EligibleProgramResponse>> getEligiblePrograms(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam("searchfor") String searchFor) {

        List<EligibleProgramResponse> programs = uhrService.getEligibleProgramsForUser(
                userPrincipal.getId(),
                searchFor);

        return ResponseEntity.ok(programs);
    }
}