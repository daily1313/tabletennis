package com.example.tabletennis.dto.response.room;

import com.example.tabletennis.domain.room.Room;
import com.example.tabletennis.domain.room.RoomStatus;
import com.example.tabletennis.domain.room.RoomType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record RoomResponse(Integer id,
                           String title,
                           Integer hostId,
                           RoomType roomType,
                           RoomStatus status,
                           @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
                           LocalDateTime createdAt,
                           @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
                           LocalDateTime updatedAt) {
    public static RoomResponse from(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getTitle(),
                room.getHost().getId(),
                room.getRoomType(),
                room.getStatus(),
                room.getCreatedAt(),
                room.getUpdatedAt()
        );
    }
}
