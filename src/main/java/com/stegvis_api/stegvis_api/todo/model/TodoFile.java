package com.stegvis_api.stegvis_api.todo.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
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
@Document(collection = "todoFiles")
public class TodoFile {

    @Id
    private String id;

    private String todoId;
    private String fileName;
    private String fileUrl;
    private String fileType;
    private long fileSize;
    private Instant dateTimeUploaded;
}
