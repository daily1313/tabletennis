package com.example.tabletennis.domain.userroom;

import com.example.tabletennis.domain.room.Room;
import com.example.tabletennis.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Random;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_room")
@Entity
public class UserRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Enumerated(EnumType.STRING)
    private Team team;

    private UserRoom(User user, Room room) {
        this.user = user;
        this.room = room;
        this.team = assignRandomTeam();
    }

    public static UserRoom of(User user, Room room) {
        return new UserRoom(user, room);
    }

    private Team assignRandomTeam() {
        return new Random().nextBoolean() ? Team.RED : Team.BLUE;
    }

    public void changeTeam(Team newTeam) {
        this.team = newTeam;
    }
}
