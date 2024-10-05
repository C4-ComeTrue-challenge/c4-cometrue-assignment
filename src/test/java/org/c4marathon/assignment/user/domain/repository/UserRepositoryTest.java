package org.c4marathon.assignment.user.domain.repository;
import static org.assertj.core.api.Assertions.assertThat;

import org.c4marathon.assignment.IntegrationTestSupport;
import org.c4marathon.assignment.user.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class UserRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();

    }

    @DisplayName("중복된 닉네임을 가진 유저가 있으면 true을 반환한다.")
    @Test
    void validateDuplicateNickname_1() {
        // given
        User user = User.create("test1@test.com", "1234", "opix");

        userRepository.save(user);

        // when
        boolean result = userRepository.existsByNickname("opix");

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("중복된 닉네임을 가진 유저가 없으면 false을 반환한다.")
    @Test
    void validateDuplicateNickname_2() {
        // given
        User user = User.create("test1@test.com", "1234", "opix");

        userRepository.save(user);

        // when
        boolean result = userRepository.existsByNickname("opixx");

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("중복된 이메일 가진 유저가 있으면 true을 반환한다.")
    @Test
    void validateDuplicateEmail_1() {
        // given
        User user = User.create("test1@test.com", "1234", "opix");

        userRepository.save(user);

        // when
        boolean result = userRepository.existsByEmail("test1@test.com");

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("중복된 이메일을 가진 유저가 없으면 false을 반환한다.")
    @Test
    void validateDuplicateEmail_2() {
        // given
        User user = User.create("test1@test.com", "1234", "opix");

        userRepository.save(user);

        // when
        boolean result = userRepository.existsByEmail("test2@test.com");

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("이메일로 유저를 찾는다")
    @Test
    void findUserByEmail() {
        // given
        User user = User.create("test@test.com", "1234", "test");
        userRepository.save(user);

        // when
        Optional<User> findUser = userRepository.findByEmail("test@test.com");

        // then
        assertThat(findUser.get())
                .extracting("email", "password", "nickname")
                .contains("test@test.com", "1234", "test");
    }
}