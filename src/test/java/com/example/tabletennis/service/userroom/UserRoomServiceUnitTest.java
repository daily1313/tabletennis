package com.example.tabletennis.service.userroom;


import com.example.tabletennis.domain.room.Room;
import com.example.tabletennis.domain.room.RoomType;
import com.example.tabletennis.domain.user.User;
import com.example.tabletennis.domain.userroom.Team;
import com.example.tabletennis.domain.userroom.UserRoom;
import com.example.tabletennis.dto.request.userroom.GameStartRequest;
import com.example.tabletennis.dto.request.userroom.RoomJoinRequest;
import com.example.tabletennis.dto.request.userroom.RoomLeaveRequest;
import com.example.tabletennis.dto.request.userroom.TeamChangeRequest;
import com.example.tabletennis.exception.room.RoomFullException;
import com.example.tabletennis.exception.room.RoomNotFoundException;
import com.example.tabletennis.exception.user.UserAlreadyInRoomException;
import com.example.tabletennis.exception.user.UserNotFoundException;
import com.example.tabletennis.exception.userroom.TeamFullException;
import com.example.tabletennis.repository.room.RoomRepository;
import com.example.tabletennis.repository.user.UserRepository;
import com.example.tabletennis.repository.userroom.UserRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.TaskScheduler;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class UserRoomServiceUnitTest {

    @InjectMocks
    private UserRoomService userRoomService;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoomRepository userRoomRepository;

    @Mock
    private TaskScheduler taskScheduler;

    private User user;
    private Room singleRoom;
    private Room doubleRoom;
    private UserRoom userRoom;
    private UserRoom doubleUserRoom;

    @BeforeEach
    void setUp() {
        user = User.of(1, "TestUser", "test@example.com");
        singleRoom = Room.of("title",  user, RoomType.SINGLE);
        doubleRoom = Room.of("title",  user, RoomType.DOUBLE);
        userRoom = UserRoom.of(user, singleRoom, Team.RED);
        doubleUserRoom = UserRoom.of(user, doubleRoom, Team.RED);
    }

    @Test
    void 유저가_방에_참여한다() {
        // given
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(roomRepository.findById(any())).willReturn(Optional.of(singleRoom));
        given(userRoomRepository.findByRoom(singleRoom)).willReturn(Collections.emptyList());

        // when
        userRoomService.joinRoom(1, new RoomJoinRequest(user.getId()));

        // then
        then(userRoomRepository).should().save(any(UserRoom.class));
    }

    @Test
    void 방이_꽉차면_예외가_발생한다() {
        // given
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(roomRepository.findById(any())).willReturn(Optional.of(singleRoom));
        given(userRoomRepository.countByRoom(singleRoom)).willReturn((long) singleRoom.getMaxCapacity());

        // when, then
        assertThatThrownBy(() -> userRoomService.joinRoom(1, new RoomJoinRequest(user.getId())))
                .isInstanceOf(RoomFullException.class);
    }

    @Test
    void 이미_참여한_유저는_예외가_발생한다() {
        // given
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(roomRepository.findById(any())).willReturn(Optional.of(singleRoom));
        given(userRoomRepository.existsByUser(user)).willReturn(true);

        // when, then
        assertThatThrownBy(() -> userRoomService.joinRoom(1, new RoomJoinRequest(user.getId())))
                .isInstanceOf(UserAlreadyInRoomException.class);
    }

//    @Test
//    void 방을_나간다() {
//        // given
//        given(userRepository.findById(any())).willReturn(Optional.of(user));
//        given(roomRepository.findById(any())).willReturn(Optional.of(singleRoom));
//        given(userRoomRepository.findByUserAndRoom(user, singleRoom)).willReturn(Optional.of(userRoom));
//
//        // when
//        userRoomService.leaveRoom(1, new RoomLeaveRequest(user.getId()));
//
//        // then
//        then(userRoomRepository).should().delete(userRoom);
//    }

//    @Test
//    void 호스트가_게임을_시작한다() {
//        // given
//        given(userRepository.findById(anyInt())).willReturn(Optional.of(user));
//        given(roomRepository.findById(anyInt())).willReturn(Optional.of(singleRoom));
//        given(userRoomRepository.countByRoom(singleRoom)).willReturn((long) singleRoom.getMaxCapacity());
//        given(singleRoom.isHost(any(User.class))).willReturn(true);
//
//        // when
//        userRoomService.startGame(1, new GameStartRequest(user.getId()));
//
//        // then
//        then(roomRepository).should().save(singleRoom);
//        then(taskScheduler).should().schedule(any(Runnable.class), any(Instant.class));
//    }
//
    @Test
    void 팀을_변경한다() {
        // given
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(roomRepository.findById(any())).willReturn(Optional.of(doubleRoom));
        given(userRoomRepository.findByUserAndRoom(user, doubleRoom)).willReturn(Optional.of(doubleUserRoom));
        given(userRoomRepository.countByRoomAndTeam(doubleRoom, Team.BLUE)).willReturn(1L);

        // when
        userRoomService.changeTeam(1, new TeamChangeRequest(user.getId()));

        // then
        assertThat(doubleUserRoom.getTeam()).isEqualTo(Team.BLUE);
    }

    @Test
    void 팀이_꽉차면_변경이_불가능하다() {
        // given
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(roomRepository.findById(any())).willReturn(Optional.of(singleRoom));
        given(userRoomRepository.findByUserAndRoom(user, singleRoom)).willReturn(Optional.of(userRoom));
        given(userRoomRepository.countByRoomAndTeam(singleRoom, Team.BLUE)).willReturn((long) singleRoom.getMaxTeamCapacity());

        // when, then
        assertThatThrownBy(() -> userRoomService.changeTeam(1, new TeamChangeRequest(user.getId())))
                .isInstanceOf(TeamFullException.class);
    }

    @Test
    void 유저가_없으면_예외가_발생한다() {
        // given
        given(userRepository.findById(any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> userRoomService.joinRoom(1, new RoomJoinRequest(999)))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void 방이_없으면_예외가_발생한다() {
        // given
        given(userRepository.findById(any())).willReturn(Optional.of(user));
        given(roomRepository.findById(any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> userRoomService.joinRoom(1, new RoomJoinRequest(user.getId())))
                .isInstanceOf(RoomNotFoundException.class);
    }
}
