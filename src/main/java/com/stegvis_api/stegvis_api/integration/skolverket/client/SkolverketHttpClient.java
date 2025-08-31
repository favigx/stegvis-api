package com.stegvis_api.stegvis_api.integration.skolverket.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import com.stegvis_api.stegvis_api.integration.skolverket.dto.ProgramResponse;
import com.stegvis_api.stegvis_api.integration.skolverket.dto.SubjectResponse;

public interface SkolverketHttpClient {

    @GetExchange("/programs")
    ProgramResponse findAll();

    @GetExchange("/programs/{code}")
    SubjectResponse findSubjectsByCode(@PathVariable("code") String code);
}
