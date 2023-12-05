package com.github.jelmerbouma85.fm24analyzer.domain.enums;

import java.util.Arrays;

public enum Condition {

    EXCELLENT("Excellent"),
    PEAK("Peak"),
    GOOD("Good"),
    FAIR("Fair"),
    INJURED("Injured");

    private final String code;

    Condition(String code) {
        this.code = code;
    }

    public static Condition fromCode(final String code) {
        return Arrays.stream(values())
                .filter(con -> con.code.equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow();
    }
}
