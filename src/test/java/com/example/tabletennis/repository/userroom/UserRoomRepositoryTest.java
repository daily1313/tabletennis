package com.example.tabletennis.repository.userroom;

import com.example.tabletennis.domain.room.Room;
import com.example.tabletennis.domain.user.User;
import com.example.tabletennis.domain.userroom.UserRoom;
import com.example.tabletennis.fixtures.room.RoomFixture;
import com.example.tabletennis.fixtures.user.UserFixture;
import com.example.tabletennis.fixtures.userroom.UserRoomFixture;
import com.example.tabletennis.repository.room.RoomRepository;
import com.example.tabletennis.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DataJpaTest
public class UserRoomRepositoryTest {

    @Autowired
    private UserRoomRepository userRoomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    private User user;
    private Room room;

    @BeforeEach
    void setUp() {
        user = UserFixture.createActiveUser();
        userRepository.save(user);

        room = RoomFixture.createSingleRoom(user);
        roomRepository.save(room);
    }

    @Test
    void 유저와_방을_생성한후_방에_참여한다() {
        // given
        UserRoom userRoom = UserRoomFixture.createUserRoom(user, room);

        // when
        UserRoom savedUserRoom = userRoomRepository.save(userRoom);

        // then
        assertThat(userRoom.getId()).isEqualTo(savedUserRoom.getId());
    }

    @Test
    void id값을_통해_방에_참가했는지_확인한다() {
        // given
        Integer id = 1;

        UserRoom userRoom = UserRoomFixture.createUserRoom(user, room);
        userRoomRepository.save(userRoom);

        // when
        Optional<UserRoom> foundUserRoom = userRoomRepository.findById(room.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(foundUserRoom).isPresent();
            softly.assertThat(foundUserRoom.get().getId()).isEqualTo(id);
        });
    }
}
