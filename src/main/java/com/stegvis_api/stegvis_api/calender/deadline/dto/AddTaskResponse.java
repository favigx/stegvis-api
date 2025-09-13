package com.stegvis_api.stegvis_api.calender.deadline.dto;

import com.stegvis_api.stegvis_api.calender.deadline.model.enums.Type;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@Getter
@RequiredArgsConstructor
public class AddTaskResponse {

    private final String taskId;
    private final String subject;
    private final Type type;
    private final String deadline;
    private final long daysLeft;
    private final boolean pastDue;
}