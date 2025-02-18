package com.example.tabletennis.dto.request.userroom;

import jakarta.validation.constraints.NotNull;

public record GameStartRequest(@NotNull Integer userId) {
}
