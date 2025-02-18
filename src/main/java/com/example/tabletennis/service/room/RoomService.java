package com.example.tabletennis.service.room;

import com.example.tabletennis.domain.room.Room;
import com.example.tabletennis.domain.user.User;
import com.example.tabletennis.dto.request.room.RoomCreateRequest;
import com.example.tabletennis.dto.response.room.PaginatedRoomListResponse;
import com.example.tabletennis.dto.response.room.RoomResponse;
import com.example.tabletennis.exception.room.RoomNotFoundException;
import com.example.tabletennis.exception.user.UserNotActiveException;
import com.example.tabletennis.exception.user.UserNotFoundException;
import com.example.tabletennis.repository.room.RoomRepository;
import com.example.tabletennis.repository.user.UserRepository;
import com.example.tabletennis.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createRoom(RoomCreateRequest roomCreateRequest) {
        User host = userRepository.findById(roomCreateRequest.userId())
                .orElseThrow(UserNotFoundException::new);

        if(!host.isActive()) {
            throw new UserNotActiveException();
        }

        Room room = Room.of(roomCreateRequest.title(), host, roomCreateRequest.roomType());
        roomRepository.save(room);
    }

    @Transactional(readOnly = true)
    public RoomResponse findByRoomId(Integer roomId) {
        return roomRepository.findRoomByRoomId(roomId)
                .orElseThrow(RoomNotFoundException::new);
    }


    @Transactional(readOnly = true)
    public PaginatedRoomListResponse findAllRoomsWithPagination(Pageable pageable) {
        Page<RoomResponse> foundRooms = roomRepository.findAllRoomsWithPagination(pageable);

        PaginatedRoomListResponse allRooms = PaginatedRoomListResponse.of(
                foundRooms.getTotalElements(), foundRooms.getTotalPages(), foundRooms.getContent());

        return allRooms;
    }
}
