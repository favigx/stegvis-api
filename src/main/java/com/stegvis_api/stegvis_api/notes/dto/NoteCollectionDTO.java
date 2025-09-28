package com.stegvis_api.stegvis_api.notes.dto;

import java.util.List;

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
public class NoteCollectionDTO {

    private String id;
    private String name;
    private List<NoteDTO> notes;

}
