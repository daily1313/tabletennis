package com.example.tabletennis.dto.response.user;

import com.example.tabletennis.domain.user.User;

public record FakerUser(
        Integer id,
        String username,
        String email
) {
    public static User toUser(FakerUser fakerUser) {
        return User.of(fakerUser.id(), fakerUser.username(), fakerUser.email());
    }
}
