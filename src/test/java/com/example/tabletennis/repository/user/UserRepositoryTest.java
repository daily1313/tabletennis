package com.example.tabletennis.repository.user;

import com.example.tabletennis.domain.user.User;
import com.example.tabletennis.fixtures.user.UserFixture;
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
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void 유저를_저장한다() {
        // given
        User user = UserFixture.createActiveUser();

        // when
        User savedUser = userRepository.save(user);

        //then
        assertSoftly(softly -> {
            softly.assertThat(savedUser.getFakerId()).isEqualTo(user.getFakerId());
            softly.assertThat(savedUser.getId()).isEqualTo(user.getId());
            softly.assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
            softly.assertThat(savedUser.getStatus()).isEqualTo(user.getStatus());
        });
    }

    @Test
    void 유저를_id_값으로_조회한다() {
        // given
        User user = UserFixture.createActiveUser();
        userRepository.save(user);

        // when
        Optional<User> foundUser = userRepository.findById(user.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(foundUser.isPresent()).isTrue();
            softly.assertThat(foundUser.get().getFakerId()).isEqualTo(user.getFakerId());
            softly.assertThat(foundUser.get().getId()).isEqualTo(user.getId());
            softly.assertThat(foundUser.get().getEmail()).isEqualTo(user.getEmail());
            softly.assertThat(foundUser.get().getStatus()).isEqualTo(user.getStatus());
        });
    }
}
