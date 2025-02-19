package com.example.tabletennis.domain.user;

import com.example.tabletennis.fixtures.user.UserFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static com.example.tabletennis.domain.user.User.determineStatus;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class UserDomainTest {

    @Test
    void 유저가_active_상태인지_확인한다() {
        // given
        User activeUser = UserFixture.createActiveUser();

        // when & then
        assertThat(activeUser.isActive()).isTrue();
    }

    @Test
    void fakerId_값이_1이상_30이하면_active_상태인지_확인한다() {
        // given
        int firstFakerId = 1;
        int lastFakerId = 30;

        // when
        Status firstStatus = determineStatus(firstFakerId);
        Status lastStatus = determineStatus(lastFakerId);

        // then
        assertSoftly(softly -> {
            softly.assertThat(firstStatus).isSameAs(Status.ACTIVE);
            softly.assertThat(lastStatus).isSameAs(Status.ACTIVE);
        });
    }

    @Test
    void fakerId_값이_31이상_60이하면_wait_상태인지_확인한다() {
        // given
        int firstFakerId = 31;
        int lastFakerId = 60;

        // when
        Status firstStatus = determineStatus(firstFakerId);
        Status lastStatus = determineStatus(lastFakerId);

        // then
        assertSoftly(softly -> {
            softly.assertThat(firstStatus).isSameAs(Status.WAIT);
            softly.assertThat(lastStatus).isSameAs(Status.WAIT);
        });
    }

    @Test
    void fakerId_값이_61이상면_non_active_상태인지_확인한다() {
        // given
        int fakerId = 61;

        // when
        Status status = determineStatus(fakerId);

        // then
        assertThat(status).isSameAs(Status.NON_ACTIVE);
    }
}
