package com.stegvis_api.stegvis_api.calender.deadline.dto;

import java.time.ZonedDateTime;

import com.stegvis_api.stegvis_api.calender.deadline.model.enums.Type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddTaskDTO {

    private String subject;
    private Type type;
    private ZonedDateTime deadline;
}