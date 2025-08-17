package com.stegvis_api.stegvis_api.calender.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;

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
public class Task {

    @Id
    private String id;

    private String userId;
    private String subject;
    private Type type;
    private Instant deadline;
    private long daysLeft;
    private boolean pastDue;
}
