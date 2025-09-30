package com.stegvis_api.stegvis_api.integration.uhr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EligibleProgramDTO {
      private String university;
    private String city;
    private String programName;
    private String lowestUrval1Score;
    private String lowestUrval2Score;
}