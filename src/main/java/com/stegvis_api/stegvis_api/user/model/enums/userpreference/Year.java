package com.stegvis_api.stegvis_api.user.model.enums.userpreference;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Year {
    YEAR_1(1),
    YEAR_2(2),
    YEAR_3(3);

    private final int year;

    Year(int year) {
        this.year = year;
    }

    @JsonValue
    public int getYear() {
        return year;
    }

    @JsonCreator
    public static Year fromInt(int year) {
        for (Year whatYear : Year.values()) {
            if (whatYear.year == year) {
                return whatYear;
            }
        }
        throw new IllegalArgumentException("Invalid year: " + year);
    }

}
