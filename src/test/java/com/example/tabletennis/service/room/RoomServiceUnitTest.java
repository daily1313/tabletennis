package com.example.tabletennis.service.room;

import com.example.tabletennis.domain.room.Room;
import com.example.tabletennis.domain.room.RoomStatus;
import com.example.tabletennis.domain.room.RoomType;
import com.example.tabletennis.domain.user.Status;
import com.example.tabletennis.domain.user.User;
import com.example.tabletennis.dto.request.room.RoomCreateRequest;
import com.example.tabletennis.dto.response.room.PaginatedRoomListResponse;
import com.example.tabletennis.dto.response.room.RoomResponse;
import com.example.tabletennis.fixtures.room.RoomFixture;
import com.example.tabletennis.fixtures.user.UserFixture;
import com.example.tabletennis.repository.room.RoomRepository;
import com.example.tabletennis.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class RoomServiceUnitTest {

    @InjectMocks
    private RoomService roomService;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    private RoomCreateRequest roomCreateRequest;
    private User activeUser;
    private Room room;
    private List<RoomResponse> roomResponses;
    private Page<RoomResponse> paginatedRoomListResponse;

    @BeforeEach
    void setUp() {
        activeUser = UserFixture.createActiveUser();
        roomCreateRequest = new RoomCreateRequest(1, RoomType.SINGLE, "title");
        room = RoomFixture.createSingleRoom(activeUser);
        roomResponses = IntStream.rangeClosed(1, 10)
                .mapToObj(i -> new RoomResponse(
                        i,
                        "Room " + i,
                        i,
                        RoomType.SINGLE,
                        RoomStatus.WAIT,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                ))
                .collect(Collectors.toList());
    }

    @Test
    void 방을_생성한다() {
        // given
        given(userRepository.findById(anyInt())).willReturn(Optional.of(activeUser));

        // when
        roomService.createRoom(roomCreateRequest);

        // then
        then(roomRepository).should().save(any(Room.class));
    }

    @Test
    void 방을_id_값으로_조회한다() {
        // given
        given(roomRepository.findRoomByRoomId(room.getId())).willReturn(Optional.of(RoomResponse.from(room)));

        // when
        RoomResponse response = roomService.findByRoomId(room.getId());

        // then
        assertThat(response).isNotNull();
        then(roomRepository).should().findRoomByRoomId(room.getId());
    }

    @Test
    void 방_페이징_조회_테스트() {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        paginatedRoomListResponse = new PageImpl<>(roomResponses, pageable, 10);
        given(roomRepository.findAllRoomsWithPagination(pageable)).willReturn(paginatedRoomListResponse);

        // when
        PaginatedRoomListResponse response = roomService.findAllRoomsWithPagination(pageable);

        // then
        assertThat(response.totalElements()).isEqualTo(10);
        assertThat(response.totalPages()).isEqualTo(1);
        then(roomRepository).should().findAllRoomsWithPagination(pageable);
    }
}
