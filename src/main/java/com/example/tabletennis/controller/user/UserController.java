package com.example.tabletennis.controller.user;

import com.example.tabletennis.common.dto.ApiResponse;
import com.example.tabletennis.controller.annotation.SwaggerApiResponse;
import com.example.tabletennis.dto.request.user.UserInitRequest;
import com.example.tabletennis.dto.response.user.PaginatedUserListResponse;
import com.example.tabletennis.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RequestMapping
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @SwaggerApiResponse(summary = "초기화 API")
    @PostMapping("/init")
    public ApiResponse<Void> init(@Valid @RequestBody UserInitRequest userInitRequest) {
        userService.init(userInitRequest);

        return ApiResponse.success();
    }

    @SwaggerApiResponse(summary = "유저 전체 조회 API", implementation = PaginatedUserListResponse.class)
    @GetMapping("/user")
    public ApiResponse<PaginatedUserListResponse> getAllUsersWithPagination(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return ApiResponse.success(userService.getAllUsers(pageable));
    }
}
