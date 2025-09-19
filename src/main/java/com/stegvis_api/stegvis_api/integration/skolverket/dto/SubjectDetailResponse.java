package com.stegvis_api.stegvis_api.integration.skolverket.dto;

import com.stegvis_api.stegvis_api.integration.skolverket.model.Subject;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SubjectDetailResponse {

    private final Subject subject;

}
