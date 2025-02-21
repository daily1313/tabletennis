package com.example.tabletennis.controller.room;

import com.example.tabletennis.domain.room.RoomStatus;
import com.example.tabletennis.domain.room.RoomType;
import com.example.tabletennis.dto.request.room.RoomCreateRequest;
import com.example.tabletennis.dto.response.room.PaginatedRoomListResponse;
import com.example.tabletennis.dto.response.room.RoomResponse;
import com.example.tabletennis.service.room.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WebMvcTest(RoomController.class)
public class RoomControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RoomService roomService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void 방_생성_API를_호출한다() throws Exception {
        // given
        RoomCreateRequest request = new RoomCreateRequest(1, RoomType.SINGLE, "title");
        doNothing().when(roomService).createRoom(any(RoomCreateRequest.class));

        // when
        ResultActions resultActions = mockMvc.perform(post("/room")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("API 요청이 성공했습니다."));
    }

    @Test
    void 방_상세_조회_API를_호출한다() throws Exception {
        // given
        Integer roomId = 1;
        RoomResponse roomResponse = new RoomResponse(
                roomId, "Test Room", 1, RoomType.SINGLE, RoomStatus.WAIT,
                LocalDateTime.now(), LocalDateTime.now()
        );

        given(roomService.findByRoomId(roomId)).willReturn(roomResponse);

        // when
        ResultActions resultActions = mockMvc.perform(get("/room/{roomId}", roomId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("API 요청이 성공했습니다."))
                .andExpect(jsonPath("$.result.title").value("Test Room"))
                .andExpect(jsonPath("$.result.roomType").value("단식"))
                .andExpect(jsonPath("$.result.status").value("대기"));
    }

    @Test
    void 방_전체_조회_API를_호출한다() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        List<RoomResponse> roomResponses = List.of(
                new RoomResponse(1, "Room 1", 1, RoomType.SINGLE, RoomStatus.WAIT, LocalDateTime.now(), LocalDateTime.now()),
                new RoomResponse(2, "Room 2", 2, RoomType.DOUBLE, RoomStatus.PROGRESS, LocalDateTime.now(), LocalDateTime.now())
        );

        PaginatedRoomListResponse paginatedRoomListResponses = new PaginatedRoomListResponse(
                2, 1, roomResponses);
        given(roomService.findAllRoomsWithPagination(pageable)).willReturn(paginatedRoomListResponses);

        // when
        ResultActions resultActions = mockMvc.perform(get("/room")
                .param("size", "10")
                .param("page", "0")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("API 요청이 성공했습니다."))
                .andExpect(jsonPath("$.result.roomList[0].title").value("Room 1"))
                .andExpect(jsonPath("$.result.roomList[1].title").value("Room 2"));
    }
}
