package com.example.tabletennis.service.userroom;

import com.example.tabletennis.domain.room.Room;
import com.example.tabletennis.domain.user.User;
import com.example.tabletennis.domain.userroom.Team;
import com.example.tabletennis.domain.userroom.UserRoom;
import com.example.tabletennis.dto.request.userroom.GameStartRequest;
import com.example.tabletennis.dto.request.userroom.RoomJoinRequest;
import com.example.tabletennis.dto.request.userroom.RoomLeaveRequest;
import com.example.tabletennis.dto.request.userroom.TeamChangeRequest;
import com.example.tabletennis.exception.room.RoomFullException;
import com.example.tabletennis.exception.room.RoomInNotWaitStateException;
import com.example.tabletennis.exception.room.RoomNotFoundException;
import com.example.tabletennis.exception.room.RoomNotFullException;
import com.example.tabletennis.exception.user.UserAlreadyInRoomException;
import com.example.tabletennis.exception.user.UserNotActiveException;
import com.example.tabletennis.exception.user.UserNotFoundException;
import com.example.tabletennis.exception.user.UserNotHostException;
import com.example.tabletennis.exception.userroom.TeamFullException;
import com.example.tabletennis.exception.userroom.UserRoomNotFoundException;
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
        User foundUser = findUser(roomJoinRequest.userId());

        Room foundRoom = findRoom(roomId);

        validateRoomJoinCondition(foundRoom, foundUser);

        UserRoom userRoom = UserRoom.of(foundUser, foundRoom);

        userRoomRepository.save(userRoom);
    }

    private void validateRoomJoinCondition(Room foundRoom, User foundUser) {
        if(!foundRoom.isWaiting()) {
            throw new RoomInNotWaitStateException();
        }

        if(!foundUser.isActive()) {
            throw new UserNotActiveException();
        }

        boolean isUserInRoom = userRoomRepository.existsByUser(foundUser);

        if(isUserInRoom) {
            throw new UserAlreadyInRoomException();
        }

        long currentRoomCount = userRoomRepository.countByRoom(foundRoom);
        int maxCapacity = foundRoom.getMaxCapacity();

        if(currentRoomCount >= maxCapacity) {
            throw new RoomFullException();
        }
    }

    @Transactional
    public void leaveRoom(Integer roomId, RoomLeaveRequest roomLeaveRequest) {
        User foundUser = findUser(roomLeaveRequest.userId());

        Room foundRoom = findRoom(roomId);

        if(foundRoom.isInProgress() || foundRoom.isFinished()) {
            throw new RoomInNotWaitStateException();
        }

        UserRoom foundUserRoom = userRoomRepository.findByUserAndRoom(foundUser, foundRoom)
                .orElseThrow(UserRoomNotFoundException::new);

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
        User foundUser = findUser(gameStartRequest.userId());

        Room foundRoom = findRoom(roomId);

        validateGameStartConditions(foundRoom, foundUser);

        foundRoom.startGame();
        roomRepository.save(foundRoom);

        taskScheduler.schedule(() -> finishGame(roomId),
                Instant.now().plusSeconds(60));
    }

    private void validateGameStartConditions(Room room, User user) {
        if (!room.isHost(user)) {
            throw new UserNotHostException();
        }

        if (!room.isWaiting()) {
            throw new RoomInNotWaitStateException();
        }

        long currentRoomCount = userRoomRepository.countByRoom(room);
        int maxCapacity = room.getMaxCapacity();

        if (currentRoomCount < maxCapacity) {
            throw new RoomNotFullException();
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void finishGame(Integer roomId) {
        Room foundRoom = findRoom(roomId);

        if (foundRoom.isInProgress()) {
            foundRoom.finishGame();
            roomRepository.save(foundRoom);
        }
    }

    @Transactional
    public void changeTeam(Integer roomId, TeamChangeRequest teamChangeRequest) {
        User foundUser = findUser(teamChangeRequest.userId());

        Room foundRoom = findRoom(roomId);

        if(!foundRoom.isWaiting()) {
            throw new IllegalArgumentException();
        }

        UserRoom foundUserRoom = userRoomRepository.findByUserAndRoom(foundUser, foundRoom)
                .orElseThrow(UserRoomNotFoundException::new);

        Team currentTeam = foundUserRoom.getTeam();
        Team newTeam = currentTeam.getOppositeTeam();

        long currentTeamCount = userRoomRepository.countByUserAndTeam(foundUser, newTeam);
        int maxCapacity = foundRoom.getMaxTeamCapacity();

        if(currentTeamCount >= maxCapacity) {
            throw new TeamFullException();
        }

        foundUserRoom.changeTeam(newTeam);
    }

    private Room findRoom(Integer roomId) {
        Room foundRoom = roomRepository.findById(roomId)
                .orElseThrow(RoomNotFoundException::new);
        return foundRoom;
    }

    private User findUser(Integer userId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        return foundUser;
    }
}
