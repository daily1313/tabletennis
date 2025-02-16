package com.example.tabletennis.service.room;

import com.example.tabletennis.domain.room.Room;
import com.example.tabletennis.domain.user.User;
import com.example.tabletennis.dto.request.room.RoomCreateRequest;
import com.example.tabletennis.dto.response.room.RoomResponse;
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
                .orElseThrow(() -> new IllegalArgumentException());

        if(!isActivatedUser(host)) {
            throw new IllegalArgumentException();
        }

        Room room = Room.of(roomCreateRequest.title(), host, roomCreateRequest.roomType());

        roomRepository.save(room);
    }

    private boolean isActivatedUser(User user) {
        return user.getStatus().value().equals("ACTIVE");
    }

    @Transactional(readOnly = true)
    public RoomResponse findByRoomId(Integer roomId) {
        return roomRepository.findRoomByRoomId(roomId)
                .orElseThrow(() -> new IllegalArgumentException());
    }


    @Transactional(readOnly = true)
    public Page<RoomResponse> findAllRooms(Pageable pageable) {
        return roomRepository.findAllRooms(pageable);
    }
}
