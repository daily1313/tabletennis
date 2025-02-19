package com.example.tabletennis.controller.userroom;

import com.example.tabletennis.dto.response.ApiResponse;
import com.example.tabletennis.controller.annotation.SwaggerApiResponse;
import com.example.tabletennis.dto.request.userroom.GameStartRequest;
import com.example.tabletennis.dto.request.userroom.RoomJoinRequest;
import com.example.tabletennis.dto.request.userroom.RoomLeaveRequest;
import com.example.tabletennis.dto.request.userroom.TeamChangeRequest;
import com.example.tabletennis.service.userroom.UserRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping
@RequiredArgsConstructor
@RestController
public class UserRoomController {

    private final UserRoomService userRoomService;

    @SwaggerApiResponse(summary = "방 참가 API")
    @PostMapping("/room/attention/{roomId}")
    public ApiResponse<Void> joinRoom(@PathVariable("roomId") Integer roomId,
                                      @Valid @RequestBody RoomJoinRequest roomJoinRequest) {
        userRoomService.joinRoom(roomId, roomJoinRequest);

        return ApiResponse.success();
    }

    @SwaggerApiResponse(summary = "방 나가기 API")
    @PostMapping("/room/out/{roomId}")
    public ApiResponse<Void> leaveRoom(@PathVariable("roomId") Integer roomId,
                                       @Valid @RequestBody RoomLeaveRequest roomLeaveRequest) {
        userRoomService.leaveRoom(roomId, roomLeaveRequest);

        return ApiResponse.success();
    }

    @SwaggerApiResponse(summary = "게임시작 API")
    @PutMapping("/room/start/{roomId}")
    public ApiResponse<Void> startGame(@PathVariable("roomId") Integer roomId,
                                       @Valid @RequestBody GameStartRequest gameStartRequest) {
        userRoomService.startGame(roomId, gameStartRequest);

        return ApiResponse.success();
    }

    @SwaggerApiResponse(summary = "팀 변경 API")
    @PutMapping("/team/{roomId}")
    public ApiResponse<Void> changeTeam(@PathVariable("roomId") Integer roomId,
                                        @Valid @RequestBody TeamChangeRequest teamChangeRequest) {
        userRoomService.changeTeam(roomId, teamChangeRequest);

        return ApiResponse.success();
    }
}
