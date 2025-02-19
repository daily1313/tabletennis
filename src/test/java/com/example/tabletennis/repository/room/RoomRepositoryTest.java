package com.example.tabletennis.repository.room;

import com.example.tabletennis.domain.room.Room;
import com.example.tabletennis.domain.user.User;
import com.example.tabletennis.fixtures.room.RoomFixture;
import com.example.tabletennis.fixtures.user.UserFixture;
import com.example.tabletennis.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DataJpaTest
public class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    private User host;

    @BeforeEach
    void setUp() {
        host = UserFixture.createActiveUser();
        userRepository.save(host);
    }

    @Test
    void 유저가_방을_생성한다() {
        // given
        Room room = RoomFixture.createSingleRoom(host);

        // when
        Room savedRoom = roomRepository.save(room);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedRoom.getId()).isEqualTo(room.getId());
            softly.assertThat(savedRoom.getHost()).isSameAs(host);
            softly.assertThat(savedRoom.getTitle()).isEqualTo(room.getTitle());
        });

    }

    @Test
    void 방을_id_값으로_조회한다() {
        // given
        Room room = RoomFixture.createSingleRoom(host);
        roomRepository.save(room);

        // when
        Optional<Room> foundRoom = roomRepository.findById(room.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(foundRoom.get().getId()).isEqualTo(room.getId());
            softly.assertThat(foundRoom.get().getHost()).isEqualTo(room.getHost());
            softly.assertThat(foundRoom.get().getTitle()).isEqualTo(room.getTitle());
        });
    }

}
