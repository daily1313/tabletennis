package com.example.tabletennis.controller.userroom;

import com.example.tabletennis.dto.request.userroom.GameStartRequest;
import com.example.tabletennis.dto.request.userroom.RoomJoinRequest;
import com.example.tabletennis.dto.request.userroom.RoomLeaveRequest;
import com.example.tabletennis.dto.request.userroom.TeamChangeRequest;
import com.example.tabletennis.service.userroom.UserRoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@WebMvcTest(UserRoomController.class)
public class UserRoomControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRoomService userRoomService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void 방_참가_API를_호출한다() throws Exception {
        // given
        Integer roomId = 1;
        Integer userId = 1;
        RoomJoinRequest request = new RoomJoinRequest(userId);
        doNothing().when(userRoomService).joinRoom(anyInt(), any(RoomJoinRequest.class));

        // when
        ResultActions resultActions = mockMvc.perform(post("/room/attention/{roomId}", roomId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("API 요청이 성공했습니다."));
    }

    @Test
    void 방_나가기_API를_호출한다() throws Exception {
        // given
        Integer roomId = 1;
        RoomLeaveRequest request = new RoomLeaveRequest(1);
        doNothing().when(userRoomService).leaveRoom(anyInt(), any(RoomLeaveRequest.class));

        // when
        ResultActions resultActions = mockMvc.perform(post("/room/out/{roomId}", roomId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("API 요청이 성공했습니다."));
    }

    @Test
    void 게임_시작_API를_호출한다() throws Exception {
        // given
        Integer roomId = 1;
        Integer userId = 1;
        GameStartRequest request = new GameStartRequest(userId);
        doNothing().when(userRoomService).startGame(anyInt(), any(GameStartRequest.class));

        // when
        ResultActions resultActions = mockMvc.perform(put("/room/start/{roomId}", roomId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("API 요청이 성공했습니다."));
    }

    @Test
    void 팀_변경_API를_호출한다() throws Exception {
        // given
        Integer roomId = 1;
        TeamChangeRequest request = new TeamChangeRequest(1);
        doNothing().when(userRoomService).changeTeam(anyInt(), any(TeamChangeRequest.class));

        // when
        ResultActions resultActions = mockMvc.perform(put("/team/{roomId}", roomId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("API 요청이 성공했습니다."));
    }
}
