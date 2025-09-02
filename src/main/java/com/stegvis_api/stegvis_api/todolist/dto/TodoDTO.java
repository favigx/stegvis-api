package com.stegvis_api.stegvis_api.todolist.dto;

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
public class TodoDTO {

    private String id;
    private String todo;
    private String dateTimeCreated;

}
