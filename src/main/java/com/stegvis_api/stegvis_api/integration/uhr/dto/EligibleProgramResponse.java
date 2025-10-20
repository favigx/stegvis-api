package com.stegvis_api.stegvis_api.integration.uhr.dto;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class EligibleProgramResponse {

    private final String universitet;
    private final String ort;
    private final String programnamn;
    private final Map<String, TermData> terminer;
    private final String antagningUrl;

    @Builder
    @Getter
    public static class TermData {
        private final String termin;
        private final String termId;
        private final String lägstaPoängUrval1;
        private final String lägstaPoängUrval2;

    }

}
