package com.stegvis_api.stegvis_api.calender.dto;

import com.stegvis_api.stegvis_api.calender.model.enums.Type;

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
public class AddTaskResponse {

    private String taskId;
    private String subject;
    private Type type;
    private String deadline;
    private long daysLeft;
    private boolean pastDue;
}
