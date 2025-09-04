package com.stegvis_api.stegvis_api.notes.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NoteDTO {

    private String id;
    private String note;
    private String subject;
    private String dateTimeCreated;
    private String dateTimeUpdated;
}
