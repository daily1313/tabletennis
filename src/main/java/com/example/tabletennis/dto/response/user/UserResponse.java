package com.example.tabletennis.dto.response.user;

import com.example.tabletennis.domain.user.Status;
import com.example.tabletennis.domain.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record UserResponse(
        Integer id,
        Integer fakerId,
        String name,
        String email,
        Status status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt
) {
        public static UserResponse from(User user) {
                return new UserResponse(
                        user.getId(),
                        user.getFakerId(),
                        user.getName(),
                        user.getEmail(),
                        user.getStatus(),
                        user.getCreatedAt(),
                        user.getUpdatedAt()
                );
        }
}
