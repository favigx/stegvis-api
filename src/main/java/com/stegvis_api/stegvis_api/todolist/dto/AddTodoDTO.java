package com.stegvis_api.stegvis_api.todolist.dto;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddTodoDTO {

    private String todo;
    private ZonedDateTime dateTimeCreated;
}
