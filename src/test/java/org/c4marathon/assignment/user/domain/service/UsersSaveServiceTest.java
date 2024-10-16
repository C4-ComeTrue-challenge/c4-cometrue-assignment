package org.c4marathon.assignment.user.domain.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.c4marathon.assignment.user.domain.Users;
import org.c4marathon.assignment.user.domain.repository.UserJpaRepository;
import org.c4marathon.assignment.user.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UsersSaveServiceTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserJpaRepository userJpaRepository;

	@AfterEach
	void tearDown() {
		userJpaRepository.deleteAllInBatch();
	}

	@DisplayName("사용자를 성공적으로 저장한다.")
	@Test
	void saveUserSuccess() {
		// Given
		Users users = org.c4marathon.assignment.user.domain.Users.builder()
			.email("test@test.com")
			.password("password")
			.nickname("testNickname")
			.build();

		// When
		Users savedUsers = userRepository.save(users);

		// Then
		assertThat(savedUsers).isNotNull();
		assertThat(savedUsers.getId()).isNotNull();
		assertThat(savedUsers.getEmail()).isEqualTo("test@test.com");
		assertThat(savedUsers.getNickname()).isEqualTo("testNickname");

		// 데이터베이스에 저장되었는지 확인
		Optional<Users> foundUser = userJpaRepository.findById(savedUsers.getId());
		assertThat(foundUser).isPresent();
		assertThat(foundUser.get().getEmail()).isEqualTo("test@test.com");
	}

	@DisplayName("null 이메일을 저장하려고 할 때 예외가 발생한다.")
	@Test
	void saveUserWithNullEmail() {
		// Given
		Users users = org.c4marathon.assignment.user.domain.Users.builder()
			.password("password")
			.nickname("testNickname")
			.build();
		// When & Then
		assertThatThrownBy(() -> userRepository.save(users))
			.isInstanceOf(DataIntegrityViolationException.class)
			.hasMessageContaining("could not execute statement");
	}

	@DisplayName("null 닉네임을 저장하려고 할 때 예외가 발생한다.")
	@Test
	void saveUserWithNullNickname() {
		// Given
		Users users = org.c4marathon.assignment.user.domain.Users.builder()
			.email("test@test.com")
			.password("password")
			.build();

		// When & Then
		assertThatThrownBy(() -> userRepository.save(users))
			.isInstanceOf(DataIntegrityViolationException.class)
			.hasMessageContaining("could not execute statement");
	}

	@DisplayName("null 비밀번호를 저장하려고 할 때 예외가 발생한다.")
	@Test
	void saveUserWithNullPassword() {
		// Given
		Users users = org.c4marathon.assignment.user.domain.Users.builder()
			.email("test@test.com")
			.nickname("testNickname")
			.build();

		// When & Then
		assertThatThrownBy(() -> userRepository.save(users))
			.isInstanceOf(DataIntegrityViolationException.class)
			.hasMessageContaining("could not execute statement");
	}
}
