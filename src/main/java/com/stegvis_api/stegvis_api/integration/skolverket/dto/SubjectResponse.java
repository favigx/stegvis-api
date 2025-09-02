package com.stegvis_api.stegvis_api.integration.skolverket.dto;

import com.stegvis_api.stegvis_api.integration.skolverket.model.ProgramDetails;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SubjectResponse {

    private final ProgramDetails program;
}
