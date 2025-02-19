package com.example.tabletennis.fixtures.room;

import com.example.tabletennis.domain.room.Room;
import com.example.tabletennis.domain.room.RoomType;
import com.example.tabletennis.domain.user.User;

public class RoomFixture {

    public static Room createSingleRoom(User host) {
        return Room.of("title", host, RoomType.SINGLE);
    }

    public static Room createDoubleRoom(User host) {
        return Room.of("title", host, RoomType.DOUBLE);
    }
}
