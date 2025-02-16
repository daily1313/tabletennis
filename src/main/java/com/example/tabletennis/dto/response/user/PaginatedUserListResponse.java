package com.example.tabletennis.dto.response.user;

import java.util.List;


public record PaginatedUserListResponse(long totalElements,
                                        int totalPages,
                                        List<UserResponse> userList) {

    public static PaginatedUserListResponse of(long totalElements,
                                               int totalPages,
                                               List<UserResponse> userList) {
        return new PaginatedUserListResponse(totalElements, totalPages, userList);
    }
}
