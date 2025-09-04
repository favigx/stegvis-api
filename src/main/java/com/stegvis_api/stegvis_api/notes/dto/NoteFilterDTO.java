package com.stegvis_api.stegvis_api.notes.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NoteFilterDTO {
    private String subject;
    private Instant fromDate;
    private Instant toDate;
    private String sortBy = "date";
    private boolean ascending = true;
}
