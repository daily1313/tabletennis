package com.example.tabletennis.service.userroom;

import com.example.tabletennis.domain.room.Room;
import com.example.tabletennis.domain.user.User;
import com.example.tabletennis.domain.userroom.Team;
import com.example.tabletennis.domain.userroom.UserRoom;
import com.example.tabletennis.dto.request.userroom.GameStartRequest;
import com.example.tabletennis.dto.request.userroom.RoomJoinRequest;
import com.example.tabletennis.dto.request.userroom.RoomLeaveRequest;
import com.example.tabletennis.dto.request.userroom.TeamChangeRequest;
import com.example.tabletennis.repository.room.RoomRepository;
import com.example.tabletennis.repository.user.UserRepository;
import com.example.tabletennis.repository.userroom.UserRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@RequiredArgsConstructor
@Service
public class UserRoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final UserRoomRepository userRoomRepository;
    private final TaskScheduler taskScheduler;

    @Transactional
    public void joinRoom(Integer roomId, RoomJoinRequest roomJoinRequest) {
        User foundUser = userRepository.findById(roomJoinRequest.userId())
                .orElseThrow(() -> new IllegalArgumentException());

        Room foundRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException());

        if(!foundRoom.isWaiting() || !foundUser.isActive()) {
            throw new IllegalArgumentException();
        }

        boolean isUserInRoom = userRoomRepository.existsByUser(foundUser);

        if(isUserInRoom) {
            throw new IllegalArgumentException();
        }

        long currentRoomCount = userRoomRepository.countByRoom(foundRoom);
        int maxCapacity = foundRoom.getMaxCapacity();

        if(currentRoomCount >= maxCapacity) {
            throw new IllegalArgumentException();
        }

        UserRoom userRoom = UserRoom.of(foundUser, foundRoom);

        userRoomRepository.save(userRoom);
    }

    @Transactional
    public void leaveRoom(Integer roomId, RoomLeaveRequest roomLeaveRequest) {
        User foundUser = userRepository.findById(roomLeaveRequest.userId())
                .orElseThrow(() -> new IllegalArgumentException());

        Room foundRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException());

        if(foundRoom.isInProgress() || foundRoom.isFinished()) {
            throw new IllegalArgumentException();
        }

        UserRoom foundUserRoom = userRoomRepository.findByUserAndRoom(foundUser, foundRoom)
                .orElseThrow(() -> new IllegalArgumentException());

        if(!foundRoom.isHost(foundUser)) {
            userRoomRepository.delete(foundUserRoom);
            return;
        }

        userRoomRepository.deleteByRoom(foundRoom);
        foundRoom.finishGame();
        roomRepository.save(foundRoom);
    }

    @Transactional
    public void startGame(Integer roomId, GameStartRequest gameStartRequest) {
        User foundUser = userRepository.findById(gameStartRequest.userId())
                .orElseThrow(() -> new IllegalArgumentException());

        Room foundRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException());

        validateGameStartConditions(foundRoom, foundUser);

        foundRoom.startGame();
        roomRepository.save(foundRoom);

        taskScheduler.schedule(() -> finishGame(roomId),
                Instant.now().plusSeconds(60));
    }

    private void validateGameStartConditions(Room room, User user) {
        if (!room.isHost(user)) {
            throw new IllegalStateException();
        }

        if (!room.isWaiting()) {
            throw new IllegalStateException();
        }

        long currentRoomCount = userRoomRepository.countByRoom(room);
        int maxCapacity = room.getMaxCapacity();

        if (currentRoomCount < maxCapacity) {
            throw new IllegalStateException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void finishGame(Integer roomId) {
        Room foundRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException());

        if (foundRoom.isInProgress()) {
            foundRoom.finishGame();
            roomRepository.save(foundRoom);
        }
    }

    @Transactional
    public void changeTeam(Integer roomId, TeamChangeRequest teamChangeRequest) {
        User foundUser = userRepository.findById(teamChangeRequest.userId())
                .orElseThrow(() -> new IllegalArgumentException());

        Room foundRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException());

        if(!foundRoom.isWaiting()) {
            throw new IllegalArgumentException();
        }

        UserRoom foundUserRoom = userRoomRepository.findByUserAndRoom(foundUser, foundRoom)
                .orElseThrow(() -> new IllegalArgumentException());

        Team currentTeam = foundUserRoom.getTeam();
        Team newTeam = currentTeam.getOppositeTeam();

        long currentTeamCount = userRoomRepository.countByUserAndTeam(foundUser, newTeam);
        int maxCapacity = foundRoom.getMaxTeamCapacity();

        if(currentTeamCount >= maxCapacity) {
            throw new IllegalArgumentException();
        }

        foundUserRoom.changeTeam(newTeam);
    }
}
