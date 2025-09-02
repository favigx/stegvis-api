package com.stegvis_api.stegvis_api.calender.deadline.dto;

import com.stegvis_api.stegvis_api.calender.deadline.model.enums.Type;

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
public class TaskDTO {
    private String id;
    private String subject;
    private Type type;
    private String deadline;
    private long daysLeft;
    private boolean pastDue;
}
