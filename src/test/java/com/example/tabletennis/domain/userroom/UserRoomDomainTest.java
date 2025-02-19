package com.example.tabletennis.domain.userroom;

import com.example.tabletennis.domain.room.Room;
import com.example.tabletennis.domain.room.RoomStatus;
import com.example.tabletennis.domain.user.User;
import com.example.tabletennis.fixtures.room.RoomFixture;
import com.example.tabletennis.fixtures.user.UserFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class UserRoomDomainTest {

    private User user;
    private Room room;

    @BeforeEach
    void setUp() {
        user = UserFixture.createActiveUser();
        room = RoomFixture.createSingleRoom(user);
    }

    @Test
    void RED_OR_BLUE_팀을_배정한다() {
        // given
        UserRoom userRoom = UserRoom.of(user, room);

        // when & then
        assertThat(userRoom.getTeam()).isIn(Team.BLUE, Team.RED);
    }

    @Test
    void 팀을_변경한다() {
        // given
        UserRoom userRoom = UserRoom.of(user, room);
        Team newTeam = userRoom.getTeam().getOppositeTeam();

        // when
        userRoom.changeTeam(newTeam);

        // then
        assertThat(userRoom.getTeam()).isEqualTo(newTeam);
    }
}
