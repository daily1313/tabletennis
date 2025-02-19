package com.example.tabletennis.fixtures.userroom;

import com.example.tabletennis.domain.room.Room;
import com.example.tabletennis.domain.user.User;
import com.example.tabletennis.domain.userroom.UserRoom;

public class UserRoomFixture {

    public static UserRoom createUserRoom(User user, Room room) {
        return UserRoom.of(user, room);
    }
}
