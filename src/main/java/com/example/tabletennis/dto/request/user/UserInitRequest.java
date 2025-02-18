package com.example.tabletennis.dto.request.user;

import jakarta.validation.constraints.NotNull;

public record UserInitRequest(@NotNull Integer seed,
                              @NotNull Integer quantity) {
}
