package com.stegvis_api.stegvis_api.integration.uhr.service;

import com.stegvis_api.stegvis_api.exception.type.ResourceNotFoundException;
import com.stegvis_api.stegvis_api.integration.uhr.client.UHRHttpClient;
import com.stegvis_api.stegvis_api.integration.uhr.model.ProgramResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Slf4j
@Service
public class UHRService {

    private final UHRHttpClient uhrHttpClient;

    public UHRService(UHRHttpClient uhrHttpClient) {
        this.uhrHttpClient = uhrHttpClient;
    }

    public ProgramResponse searchPrograms(String searchFor,
            String searchTerm,
            String searchCategory,
            Integer pageSize,
            Integer page,
            String tillfalle) {
        try {
            ProgramResponse response = uhrHttpClient.searchTotal(searchFor, searchTerm, searchCategory, pageSize, page,
                    tillfalle);
            if (response.getData() == null || response.getData().isEmpty()) {
                throw new ResourceNotFoundException("Inga program hittades");
            }

            log.info("Successfully fetched programs from UHR API for searchFor={}, searchTerm={}", searchFor,
                    searchTerm);
            return response;

        } catch (HttpClientErrorException.NotFound e) {
            log.warn("UHR API returned 404 for searchFor={} and searchTerm={}", searchFor, searchTerm);
            throw new ResourceNotFoundException("Inga program hittades för dina kriterier");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error calling UHR API for searchFor={}, searchTerm={}: {}", searchFor, searchTerm,
                    e.getMessage(), e);
            throw new RuntimeException("Fel vid anrop till UHR API");
        }
    }
}