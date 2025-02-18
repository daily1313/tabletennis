package com.example.tabletennis.dto.request.room;

import com.example.tabletennis.domain.room.RoomType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoomCreateRequest(@NotNull Integer userId,
                                RoomType roomType,
                                @NotBlank String title) {
}
