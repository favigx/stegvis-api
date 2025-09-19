package com.stegvis_api.stegvis_api.integration.skolverket.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.stegvis_api.stegvis_api.exception.type.ResourceNotFoundException;
import com.stegvis_api.stegvis_api.integration.skolverket.client.SkolverketHttpClient;
import com.stegvis_api.stegvis_api.integration.skolverket.dto.ProgramResponse;
import com.stegvis_api.stegvis_api.integration.skolverket.dto.SubjectCourseResponse;
import com.stegvis_api.stegvis_api.integration.skolverket.dto.SubjectDetailResponse;
import com.stegvis_api.stegvis_api.integration.skolverket.dto.SubjectResponse;
import com.stegvis_api.stegvis_api.integration.skolverket.model.Course;
import com.stegvis_api.stegvis_api.integration.skolverket.model.CourseInfo;
import com.stegvis_api.stegvis_api.integration.skolverket.model.SubjectInfo;

@Slf4j
@Service
public class SkolverketService {

    private final SkolverketHttpClient skolverketHttpClient;

    public SkolverketService(SkolverketHttpClient skolverketHttpClient) {
        this.skolverketHttpClient = skolverketHttpClient;
    }

    public ProgramResponse getAllPrograms() {
        try {
            ProgramResponse response = skolverketHttpClient.findAll();
            log.info("Successfully fetched all programs from Skolverket API");
            return response;
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("No programs found in Skolverket API");
            throw new ResourceNotFoundException("Program kunde inte hittas");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error calling Skolverket API: {}", e.getMessage(), e);
            throw new RuntimeException("Fel vid anrop till Skolverket API");
        }
    }

    public SubjectResponse getSubjectsByProgramCode(String code) {
        try {
            SubjectResponse response = skolverketHttpClient.findSubjectsByCode(code);
            log.info("Successfully fetched subjects for programCode={}", code);
            return response;
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Subjects not found for programCode={}", code);
            throw new ResourceNotFoundException("Subjects för programkod " + code + " kunde inte hittas");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error calling Skolverket API for programCode={}: {}", code, e.getMessage(), e);
            throw new RuntimeException("Fel vid anrop till Skolverket API");
        }
    }

    public SubjectCourseResponse getSubjectCourseDetails(String subjectCode, String courseCode) {
        try {
            SubjectDetailResponse subjectResponse = skolverketHttpClient.findSubjectByCode(subjectCode);
            var subject = subjectResponse.getSubject();

            Course course = subject.getCourses().stream()
                    .filter(c -> c.getCode().equals(courseCode))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Kurs " + courseCode + " kunde inte hittas för ämnet " + subjectCode));

            return new SubjectCourseResponse(
                    new SubjectInfo(subject.getCode(), subject.getName(), subject.getDescription(),
                            subject.getPurpose()),
                    new CourseInfo(course.getCode(), course.getName(), course.getDescription(),
                            course.getCentralContent(), course.getKnowledgeRequirements()));

        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Subject details not found for subjectCode={}", subjectCode);
            throw new ResourceNotFoundException("Subjects för programkod " + subjectCode + " kunde inte hittas");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error calling Skolverket API for subjectCode={}: {}", subjectCode, e.getMessage(), e);
            throw new RuntimeException("Fel vid anrop till Skolverket API");
        }
    }

}