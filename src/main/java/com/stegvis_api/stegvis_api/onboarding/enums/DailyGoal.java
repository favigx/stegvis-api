package com.stegvis_api.stegvis_api.onboarding.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DailyGoal {
    MINUTES_20(20),
    MINUTES_30(30),
    MINUTES_45(45),
    MINUTES_60(60),
    MINUTES_80(80),
    MINUTES_100(100),
    MINUTES_120(120);

    private final int minutes;

    DailyGoal(int minutes) {
        this.minutes = minutes;
    }

    @JsonValue
    public int getMinutes() {
        return minutes;
    }

    @JsonCreator
    public static DailyGoal fromInt(int minutes) {
        for (DailyGoal goal : DailyGoal.values()) {
            if (goal.minutes == minutes) {
                return goal;
            }
        }
        throw new IllegalArgumentException("Invalid daily goal: " + minutes);
    }
}