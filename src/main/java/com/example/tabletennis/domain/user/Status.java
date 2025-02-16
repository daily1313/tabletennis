package com.example.tabletennis.domain.user;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {

    WAIT("WAIT"),
    ACTIVE("ACTIVE"),
    NON_ACTIVE("NON_ACTIVE");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }
}
