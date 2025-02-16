package com.example.tabletennis.dto.request.room;

import com.example.tabletennis.domain.room.RoomType;

public record RoomCreateRequest(Integer userId,
                                RoomType roomType,
                                String title) {
}
