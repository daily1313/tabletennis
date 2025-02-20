package com.example.tabletennis.domain.room;

import com.example.tabletennis.domain.user.User;
import com.example.tabletennis.fixtures.room.RoomFixture;
import com.example.tabletennis.fixtures.user.UserFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class RoomDomainTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = UserFixture.createActiveUser();
    }

    @Test
    void 방을_처음_생성하면_wait_상태인지_확인한다() {
        // given
        Room room = RoomFixture.createSingleRoom(user);

        // when & then
        assertThat(room.isWaiting()).isTrue();
    }

    @Test
    void 게임을_시작하면_방_상태가_PROGRESS로_변경된다() {
        // given
        Room room = RoomFixture.createSingleRoom(user);

        // when
        room.startGame();

        // then
        assertThat(room.isInProgress()).isTrue();
    }

    @Test
    void 방이_SINGLE_상태면_인원수는_2명이다() {
        // given
        Room room = RoomFixture.createSingleRoom(user);

        // when & then
        assertThat(room.getMaxCapacity()).isEqualTo(2);
    }

    @Test
    void 방이_DOUBLE_상태면_인원수는_4명이다() {
        // given
        Room room = RoomFixture.createDoubleRoom(user);

        // when & then
        assertThat(room.getMaxCapacity()).isEqualTo(4);
    }

    @Test
    void 방이_SINGLE_상태면_팀당_인원수는_1명이다() {
        // given
        Room room = RoomFixture.createSingleRoom(user);

        // when & then
        assertThat(room.getMaxTeamCapacity()).isEqualTo(1);
    }

    @Test
    void 방이_DOUBLE_상태면_팀당_인원수는_2명이다() {
        // given
        Room room = RoomFixture.createDoubleRoom(user);

        // when & then
        assertThat(room.getMaxTeamCapacity()).isEqualTo(2);
    }
}
