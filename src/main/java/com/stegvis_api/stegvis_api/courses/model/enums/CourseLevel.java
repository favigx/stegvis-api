package com.stegvis_api.stegvis_api.courses.model.enums;

public enum CourseLevel {
    LEVEL_1(1),
    LEVEL_2(2),
    LEVEL_3(3);

    private final int code;

    CourseLevel(int code) {
        this.code = code;
    }

    @com.fasterxml.jackson.annotation.JsonValue
    public int getCode() {
        return code;
    }

    @com.fasterxml.jackson.annotation.JsonCreator
    public static CourseLevel fromCode(int code) {
        for (CourseLevel level : values()) {
            if (level.code == code)
                return level;
        }
        throw new IllegalArgumentException("Unknown CourseLevel code: " + code);
    }
}