package com.example.tabletennis.domain.user;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {

    WAIT("대기"),
    ACTIVE("활성"),
    NON_ACTIVE("비활성");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }
}
