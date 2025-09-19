package com.stegvis_api.stegvis_api.integration.skolverket.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stegvis_api.stegvis_api.integration.skolverket.dto.ProgramResponse;
import com.stegvis_api.stegvis_api.integration.skolverket.dto.SubjectCourseResponse;
import com.stegvis_api.stegvis_api.integration.skolverket.dto.SubjectResponse;
import com.stegvis_api.stegvis_api.integration.skolverket.service.SkolverketService;

@RestController
@RequestMapping("/api/skolverket")
public class SkolverketController {

    private final SkolverketService skolverketService;

    public SkolverketController(SkolverketService skolverketService) {
        this.skolverketService = skolverketService;
    }

    @GetMapping("/programs")
    public ResponseEntity<ProgramResponse> findAllPrograms() {
        ProgramResponse programResponse = skolverketService.getAllPrograms();
        return ResponseEntity.ok(programResponse);
    }

    @GetMapping("/programs/{code}/subjects")
    public ResponseEntity<SubjectResponse> findSubjectsForProgram(@PathVariable("code") String code) {
        SubjectResponse subjectResponse = skolverketService.getSubjectsByProgramCode(code);
        return ResponseEntity.ok(subjectResponse);
    }

    @GetMapping("/subject/{subjectCode}/course/{courseCode}")
    public ResponseEntity<SubjectCourseResponse> findSubjectCourseDetails(
            @PathVariable("subjectCode") String subjectCode,
            @PathVariable("courseCode") String courseCode) {

        SubjectCourseResponse subjectCourseResponse = skolverketService.getSubjectCourseDetails(subjectCode,
                courseCode);
        return ResponseEntity.ok(subjectCourseResponse);
    }
}
