package com.example.tabletennis.repository.room;

import com.example.tabletennis.domain.room.Room;
import com.example.tabletennis.dto.response.room.RoomResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    @Query("SELECT new com.example.tabletennis.dto.response.room.RoomResponse" +
            "(r.id, r.title, r.host.id, r.roomType, r.status, r.createdAt, r.updatedAt) FROM Room r " +
            "where r.id =: roomId"
    )
    Optional<RoomResponse> findRoomByRoomId(@Param("roomId") Integer roomId);

    @Query("SELECT new com.example.tabletennis.dto.response.room.RoomResponse" +
            "(r.id, r.title, r.host.id, r.roomType, r.status, r.createdAt, r.updatedAt) FROM Room r")
    Page<RoomResponse> findAllRooms(Pageable pageable);
}
