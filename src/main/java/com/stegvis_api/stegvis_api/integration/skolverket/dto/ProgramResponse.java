package com.stegvis_api.stegvis_api.integration.skolverket.dto;

import java.util.List;

import com.stegvis_api.stegvis_api.integration.skolverket.model.Program;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProgramResponse {

    private final List<Program> programs;

}
