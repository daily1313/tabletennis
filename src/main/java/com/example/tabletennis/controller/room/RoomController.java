package com.example.tabletennis.controller.room;

import com.example.tabletennis.common.dto.ApiResponse;
import com.example.tabletennis.dto.request.room.RoomCreateRequest;
import com.example.tabletennis.dto.response.room.RoomResponse;
import com.example.tabletennis.service.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/room")
@RequiredArgsConstructor
@RestController
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ApiResponse<Void> createRoom(@RequestBody RoomCreateRequest roomCreateRequest) {
        roomService.createRoom(roomCreateRequest);
        return ApiResponse.success();
    }

    @GetMapping("/{roomId}")
    public ApiResponse<RoomResponse> getRoomByRoomId(@PathVariable("roomId") Integer roomId) {
        RoomResponse foundRoom = roomService.findByRoomId(roomId);
        return ApiResponse.success(foundRoom);
    }

    @GetMapping
    public ApiResponse<Page<RoomResponse>> getAllRooms(@RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<RoomResponse> foundAllRooms = roomService.findAllRooms(pageable);
        return ApiResponse.success(foundAllRooms);
    }
}
