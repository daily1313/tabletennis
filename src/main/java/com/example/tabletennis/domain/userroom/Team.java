package com.example.tabletennis.domain.userroom;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Team {

    RED("RED"),
    BLUE("BLUE");

    private final String value;

    Team(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
