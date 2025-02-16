package com.example.tabletennis.domain.room;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RoomStatus {

    WAIT("WAIT"),
    ACTIVE("ACTIVE"),
    NON_ACTIVE("NON_ACTIVE");

    private final String value;

    RoomStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }
}
