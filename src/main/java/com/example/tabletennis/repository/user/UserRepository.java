package com.example.tabletennis.repository.user;

import com.example.tabletennis.domain.user.User;
import com.example.tabletennis.dto.response.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT new com.example.tabletennis.dto.response.user.UserResponse" +
            "(u.id, u.fakerId, u.name, u.email, u.status, u.createdAt, u.updatedAt) " +
            "FROM User u")
    Page<UserResponse> findAllUsersWithPagination(Pageable pageable);
}
