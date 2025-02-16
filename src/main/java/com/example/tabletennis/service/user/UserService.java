package com.example.tabletennis.service.user;

import com.example.tabletennis.domain.user.User;
import com.example.tabletennis.dto.request.user.UserInitRequest;
import com.example.tabletennis.dto.response.user.FakerApiUserResponse;
import com.example.tabletennis.dto.response.user.FakerUser;
import com.example.tabletennis.dto.response.user.PaginatedUserListResponse;
import com.example.tabletennis.dto.response.user.UserResponse;
import com.example.tabletennis.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final FakerApiService fakerApiService;

    @Transactional
    public void init(UserInitRequest userInitRequest) {
        userRepository.deleteAll();

        FakerApiUserResponse fakerApiUserResponse = fakerApiService.initUsers(userInitRequest);

        if(fakerApiUserResponse != null) {
            List<User> initializedUsers = fakerApiUserResponse.data().stream()
                    .sorted(Comparator.comparingInt(FakerUser::id))
                    .map(FakerUser::toUser)
                    .collect(Collectors.toList());

            userRepository.saveAll(initializedUsers);
        }
    }

    @Transactional(readOnly = true)
    public PaginatedUserListResponse getAllUsers(Pageable pageable) {
        Page<UserResponse> foundUsers = userRepository.findAllUsers(pageable);

        PaginatedUserListResponse allUsers = PaginatedUserListResponse.of(
                foundUsers.getTotalElements(), foundUsers.getTotalPages(), foundUsers.getContent());

        return allUsers;
    }
}
