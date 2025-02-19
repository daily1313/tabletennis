package com.example.tabletennis.domain.room;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RoomType {

    SINGLE("단식"),
    DOUBLE("복식");

    private final String value;

    RoomType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static RoomType fromValue(String value) {
        for (RoomType roomType : RoomType.values()) {
            if (roomType.getValue().equals(value)) {
                return roomType;
            }
        }
        throw new IllegalArgumentException();
    }
}
