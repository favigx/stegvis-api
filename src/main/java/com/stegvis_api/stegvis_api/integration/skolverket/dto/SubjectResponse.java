package com.stegvis_api.stegvis_api.integration.skolverket.dto;

import com.stegvis_api.stegvis_api.integration.skolverket.model.ProgramDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectResponse {

    private ProgramDetails program;
}
