package com.example.tabletennis.dto.response.room;

import java.util.List;

public record PaginatedRoomListResponse(int totalElements,
                                        int totalPages,
                                        List<RoomResponse> roomList) {
}
