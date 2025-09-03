package com.stegvis_api.stegvis_api.integration.skolverket.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.stegvis_api.stegvis_api.exception.type.ResourceNotFoundException;
import com.stegvis_api.stegvis_api.integration.skolverket.client.SkolverketHttpClient;
import com.stegvis_api.stegvis_api.integration.skolverket.dto.ProgramResponse;
import com.stegvis_api.stegvis_api.integration.skolverket.dto.SubjectResponse;

@Service
public class SkolverketService {

    private final SkolverketHttpClient skolverketHttpClient;

    public SkolverketService(SkolverketHttpClient skolverketHttpClient) {
        this.skolverketHttpClient = skolverketHttpClient;
    }

    public ProgramResponse getAllPrograms() {
        try {
            return skolverketHttpClient.findAll();
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Program kunde inte hittas");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Fel vid anrop till Skolverket API: " + e.getMessage());
        }
    }

    public SubjectResponse getSubjectsByProgramCode(String code) {
        try {
            return skolverketHttpClient.findSubjectsByCode(code);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Subjects för programkod " + code + " kunde inte hittas");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("Fel vid anrop till Skolverket API: " + e.getMessage());
        }
    }
}
