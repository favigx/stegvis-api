package com.stegvis_api.stegvis_api.integration.skolverket.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stegvis_api.stegvis_api.integration.skolverket.client.SkolverketHttpClient;
import com.stegvis_api.stegvis_api.integration.skolverket.dto.ProgramResponse;
import com.stegvis_api.stegvis_api.integration.skolverket.dto.SubjectResponse;
import com.stegvis_api.stegvis_api.integration.skolverket.service.SkolverketService;

@RestController
@RequestMapping("/api/skolverket")
public class SkolverketController {

    private final SkolverketHttpClient skolverketHttpClient;
    private final SkolverketService skolverketService;

    public SkolverketController(SkolverketHttpClient skolverketHttpClient, SkolverketService skolverketService) {
        this.skolverketHttpClient = skolverketHttpClient;
        this.skolverketService = skolverketService;
    }

    @GetMapping("/programs")
    public ResponseEntity<ProgramResponse> findAllPrograms() {
        ProgramResponse programResponse = skolverketHttpClient.findAll();

        return ResponseEntity.ok(programResponse);
    }

    @GetMapping("/programs/{code}/subjects")
    public ResponseEntity<SubjectResponse> findSubjectsForProgram(@PathVariable("code") String code) {
        SubjectResponse subjectResponse = skolverketHttpClient.findSubjectsByCode(code);

        return ResponseEntity.ok(subjectResponse);
    }
}