package com.example.tabletennis.dto.response.room;

import java.util.List;

public record PaginatedRoomListResponse(long totalElements,
                                        int totalPages,
                                        List<RoomResponse> roomList) {

    public static PaginatedRoomListResponse of(long totalElements,
                                               int totalPages,
                                               List<RoomResponse> roomList) {
        return new PaginatedRoomListResponse(totalElements, totalPages, roomList);
    }
}
