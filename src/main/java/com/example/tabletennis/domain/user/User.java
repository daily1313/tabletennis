package com.example.tabletennis.domain.user;

import com.example.tabletennis.common.AuditableEntity;
import com.example.tabletennis.exception.user.UserNotActiveException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "`user`")
public class User extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer fakerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Status status;

    private User(Integer fakerId, String name, String email) {
        this.fakerId = fakerId;
        this.name = name;
        this.email = email;
        this.status = determineStatus(fakerId);
    }

    public static User of(Integer fakerId, String name, String email) {
        return new User(fakerId, name, email);
    }

    public static Status determineStatus(int fakerId) {
        return switch ((fakerId - 1) / 30) {
            case 0 -> Status.ACTIVE;
            case 1 -> Status.WAIT;
            default -> Status.NON_ACTIVE;
        };
    }

    public void validateActiveUser() {
        if(!isActive()) throw new UserNotActiveException();
    }

    public boolean isActive() {
        return status == Status.ACTIVE;
    }
}
