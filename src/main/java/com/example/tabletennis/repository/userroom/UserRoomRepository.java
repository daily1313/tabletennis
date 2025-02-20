package com.example.tabletennis.repository.userroom;

import com.example.tabletennis.domain.room.Room;
import com.example.tabletennis.domain.user.User;
import com.example.tabletennis.domain.userroom.Team;
import com.example.tabletennis.domain.userroom.UserRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRoomRepository extends JpaRepository<UserRoom, Integer> {

    boolean existsByUser(User user);

    long countByRoom(Room room);

    Optional<UserRoom> findByUserAndRoom(User user, Room room);

    void deleteByRoom(Room room);

    long countByRoomAndTeam(Room room, Team team);

    List<UserRoom> findByRoom(Room room);
}
