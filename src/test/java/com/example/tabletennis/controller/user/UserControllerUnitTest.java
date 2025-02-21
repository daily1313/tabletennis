package com.example.tabletennis.controller.user;

import com.example.tabletennis.domain.user.Status;
import com.example.tabletennis.dto.request.user.UserInitRequest;
import com.example.tabletennis.dto.response.user.PaginatedUserListResponse;
import com.example.tabletennis.dto.response.user.UserResponse;
import com.example.tabletennis.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
@WebMvcTest(UserController.class)
public class UserControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void 유저_초기화_API를_호출한다() throws Exception {
        // given
        UserInitRequest request = new UserInitRequest(1, 1);
        doNothing().when(userService).init(any(UserInitRequest.class));

        // when
        ResultActions resultActions = mockMvc.perform(post("/init")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("API 요청이 성공했습니다."));
    }

    @Test
    void 유저_전체_조회_API를_호출한다() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        List<UserResponse> userResponses = List.of(
                new UserResponse(1, 1, "testUser1", "test@test.com", Status.ACTIVE,
                        LocalDateTime.now(), LocalDateTime.now()),
                new UserResponse(2, 2, "testUser2", "test2@test.com", Status.ACTIVE,
                        LocalDateTime.now(), LocalDateTime.now())
        );

        PaginatedUserListResponse paginatedUserListResponses = new PaginatedUserListResponse(2, 1, userResponses);
        given(userService.getAllUsers(pageable)).willReturn(paginatedUserListResponses);

        // when
        ResultActions resultActions = mockMvc.perform(get("/user")
                .param("size", "10")
                .param("page", "0")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("API 요청이 성공했습니다."))
                .andExpect(jsonPath("$.result.userList[0].name").value("testUser1"))
                .andExpect(jsonPath("$.result.userList[0].email").value("test@test.com"))
                .andExpect(jsonPath("$.result.userList[1].name").value("testUser2"))
                .andExpect(jsonPath("$.result.userList[1].email").value("test2@test.com"));
    }
}
