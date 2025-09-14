package com.stegvis_api.stegvis_api.notes.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "userNotes")
public class Note {

    @Id
    private String id;

    private String userId;
    private String note;
    private String subject;
    private Instant dateTime;
}
