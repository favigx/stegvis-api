package com.stegvis_api.stegvis_api.notes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NoteDTO {

    private String id;
    private String note;
    private String subject;
    private String dateTime;
}
