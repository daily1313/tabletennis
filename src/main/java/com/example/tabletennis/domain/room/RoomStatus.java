package com.example.tabletennis.domain.room;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RoomStatus {

    WAIT("대기"),
    PROGRESS("진행중"),
    FINISH("완료");

    private final String value;

    RoomStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }
}
