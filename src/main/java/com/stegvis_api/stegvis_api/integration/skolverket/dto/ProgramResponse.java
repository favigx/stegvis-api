package com.stegvis_api.stegvis_api.integration.skolverket.dto;

import java.util.List;

import com.stegvis_api.stegvis_api.integration.skolverket.model.Program;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProgramResponse {

    private List<Program> programs;

}
