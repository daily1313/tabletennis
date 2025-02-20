package com.example.tabletennis.service.user;

import com.example.tabletennis.domain.user.Status;
import com.example.tabletennis.domain.user.User;
import com.example.tabletennis.dto.request.user.UserInitRequest;
import com.example.tabletennis.dto.response.user.FakerApiUserResponse;
import com.example.tabletennis.dto.response.user.FakerUser;
import com.example.tabletennis.dto.response.user.PaginatedUserListResponse;
import com.example.tabletennis.dto.response.user.UserResponse;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FakerApiService fakerApiService;

    private UserInitRequest userInitRequest;
    private FakerApiUserResponse fakerApiUserResponse;
    private List<UserResponse> userResponse;
    private Page<UserResponse> paginatedUserResponse;

    @BeforeEach
    void setUp() {
        userInitRequest = new UserInitRequest(1, 10);
        List<FakerUser> fakeUsers = IntStream.rangeClosed(1, 10)
                .mapToObj(i -> new FakerUser(i, "user" + i + "@test.com", "username" + i))
                .collect(Collectors.toList());
        fakerApiUserResponse = new FakerApiUserResponse(fakeUsers);
        userResponse = IntStream.rangeClosed(1, 10)
                .mapToObj(i -> new UserResponse(
                        i,
                        i,
                        "username" + i,
                        "user" + i + "@test.com",
                        Status.ACTIVE,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                ))
                .collect(Collectors.toList());

    }

    @Test
    void 사용자_초기화를_진행한다() {
        // given
        given(fakerApiService.initUsers(userInitRequest)).willReturn(fakerApiUserResponse);

        // when
        userService.init(userInitRequest);

        // then
        then(userRepository).should().deleteAllInBatch();
        then(userRepository).should().saveAll(anyList());
    }

    @Test
    void 모든_사용자를_페이징하여_조회한다() {
        // given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        paginatedUserResponse = new PageImpl<>(userResponse, pageable, 10);
        given(userRepository.findAllUsersWithPagination(pageable)).willReturn(paginatedUserResponse);

        // when
        PaginatedUserListResponse response = userService.getAllUsers(pageable);

        // then
        assertThat(response.totalElements()).isEqualTo(10);
        assertThat(response.totalPages()).isEqualTo(1);
        then(userRepository).should().findAllUsersWithPagination(pageable);
    }
}
