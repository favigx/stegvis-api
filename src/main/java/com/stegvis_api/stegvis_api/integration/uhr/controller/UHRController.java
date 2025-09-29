package com.stegvis_api.stegvis_api.integration.uhr.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stegvis_api.stegvis_api.integration.uhr.model.ProgramResponse;
import com.stegvis_api.stegvis_api.integration.uhr.service.UHRService;

@RestController
@RequestMapping("/api/uhr")
public class UHRController {

    private final UHRService uhrService;

    public UHRController(UHRService uhrService) {
        this.uhrService = uhrService;
    }

    @GetMapping("/programs")
    public ResponseEntity<ProgramResponse> findPrograms(
            @RequestParam("searchfor") String searchFor,
            @RequestParam("searchterm") String searchTerm,
            @RequestParam(value = "searchkategori", required = false) String searchCategory,
            @RequestParam(value = "pagesize", required = false, defaultValue = "1000") Integer pageSize,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam("tillfalle") String tillfalle) {

        ProgramResponse programResponse = uhrService.searchPrograms(
                searchFor, searchTerm, searchCategory, pageSize, page, tillfalle);

        return ResponseEntity.ok(programResponse);
    }
}