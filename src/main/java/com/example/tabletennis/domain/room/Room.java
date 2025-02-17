package com.example.tabletennis.domain.room;

import com.example.tabletennis.common.AuditableEntity;
import com.example.tabletennis.domain.user.Status;
import com.example.tabletennis.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "room")
@Entity
public class Room extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User host;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    private Room(String title, User host, RoomType roomType) {
        this.title = title;
        this.host = host;
        this.roomType = roomType;
        this.status = RoomStatus.WAIT;
    }

    public static Room of(String title, User host, RoomType roomType) {
        return new Room(title, host, roomType);
    }
}
