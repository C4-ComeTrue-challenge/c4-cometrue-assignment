package org.c4marathon.assignment.user.domain.repository;

import org.c4marathon.assignment.user.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("이메일로 사용자를 성공적으로 조회한다.")
    @Test
    void findByEmailSuccess() {
        // Given
        User user = User.builder()
                .email("test@test.com")
                .password("password")
                .nickname("testNickname")
                .build();

        userRepository.save(user);

        // When
        Optional<User> foundUser = userRepository.findByEmail("test@test.com");

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("test@test.com");
        assertThat(foundUser.get().getNickname()).isEqualTo("testNickname");
    }

    @DisplayName("존재하지 않는 이메일로 조회 시 빈 결과를 반환한다.")
    @Test
    void findByEmailNotFound() {
        // When
        Optional<User> foundUser = userRepository.findByEmail("notfound@test.com");

        // Then
        assertThat(foundUser).isNotPresent();
    }

    @DisplayName("이메일 존재 여부를 확인한다.")
    @Test
    void existsByEmailSuccess() {
        // Given
        User user = User.builder()
                .email("exists@test.com")
                .password("password")
                .nickname("testNickname")
                .build();

        userRepository.save(user);

        // When
        boolean exists = userRepository.existsByEmail("exists@test.com");

        // Then
        assertThat(exists).isTrue();
    }

    @DisplayName("존재하지 않는 이메일로 존재 여부를 확인하면 false를 반환한다.")
    @Test
    void existsByEmailNotFound() {
        // When
        boolean exists = userRepository.existsByEmail("notfound@test.com");

        // Then
        assertThat(exists).isFalse();
    }

    @DisplayName("닉네임 존재 여부를 확인한다.")
    @Test
    void existsByNicknameSuccess() {
        // Given
        User user = User.builder()
                .email("test@test.com")
                .password("password")
                .nickname("testNickname")
                .build();

        userRepository.save(user);

        // When
        boolean exists = userRepository.existsByNickname("testNickname");

        // Then
        assertThat(exists).isTrue();
    }

    @DisplayName("존재하지 않는 닉네임으로 존재 여부를 확인하면 false를 반환한다.")
    @Test
    void existsByNicknameNotFound() {
        // When
        boolean exists = userRepository.existsByNickname("nonexistentNickname");

        // Then
        assertThat(exists).isFalse();
    }
}
