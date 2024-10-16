package org.c4marathon.assignment.user.domain.service;

import static org.assertj.core.api.Assertions.*;
import static org.c4marathon.assignment.global.exception.ErrorCode.*;

import org.c4marathon.assignment.user.domain.User;
import org.c4marathon.assignment.user.domain.repository.UserJpaRepository;
import org.c4marathon.assignment.user.domain.repository.UserRepository;
import org.c4marathon.assignment.user.exception.NotFoundUserException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class UserGetServiceTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserJpaRepository userJpaRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@AfterEach
	void tearDown() {
		userJpaRepository.deleteAllInBatch();
	}

	@DisplayName("이메일로 사용자를 성공적으로 조회한다.")
	@Test
	void getByEmailSuccess() {
		// Given
		User user = User.builder()
			.email("test@test.com")
			.password(encoder.encode("password"))
			.nickname("testNickname")
			.build();

		userJpaRepository.save(user);

		// When
		User foundUser = userRepository.getByEmail("test@test.com");

		// Then
		assertThat(foundUser).isNotNull();
		assertThat(foundUser.getEmail()).isEqualTo("test@test.com");
		assertThat(foundUser.getNickname()).isEqualTo("testNickname");
	}

	@DisplayName("존재하지 않는 이메일로 조회 시 예외가 발생한다.")
	@Test
	void getByEmailNotFound() {
		// When & Then
		assertThatThrownBy(() -> userRepository.getByEmail("notfound@test.com"))
			.isInstanceOf(NotFoundUserException.class)
			.hasMessageContaining(NOT_FOUND_USER_ERROR.getMessage());
	}

	@DisplayName("ID로 사용자를 성공적으로 조회한다.")
	@Test
	void getByIdSuccess() {
		// Given
		User user = User.builder()
			.email("test@test.com")
			.password(encoder.encode("password"))
			.nickname("testNickname")
			.build();

		userJpaRepository.save(user);

		// When
		User foundUser = userRepository.getById(user.getId());

		// Then
		assertThat(foundUser).isNotNull();
		assertThat(foundUser.getId()).isEqualTo(user.getId());
		assertThat(foundUser.getEmail()).isEqualTo("test@test.com");
	}

	@DisplayName("존재하지 않는 ID로 조회 시 예외가 발생한다.")
	@Test
	void getByIdNotFound() {
		// When & Then
		assertThatThrownBy(() -> userRepository.getById(999L))
			.isInstanceOf(NotFoundUserException.class)
			.hasMessageContaining(NOT_FOUND_USER_ERROR.getMessage());
	}

	@DisplayName("이메일 존재 여부를 확인한다.")
	@Test
	void existByEmailSuccess() {
		// Given
		User user = User.builder()
			.email("exists@test.com")
			.password(encoder.encode("password"))
			.nickname("testNickname")
			.build();

		userJpaRepository.save(user);

		// When
		boolean exists = userRepository.existByEmail("exists@test.com");

		// Then
		assertThat(exists).isTrue();
	}

	@DisplayName("존재하지 않는 이메일로 조회 시 존재하지 않음을 반환한다.")
	@Test
	void existByEmailNotFound() {
		// When
		boolean exists = userRepository.existByEmail("notfound@test.com");

		// Then
		assertThat(exists).isFalse();
	}

	@DisplayName("닉네임 존재 여부를 확인한다.")
	@Test
	void existByNicknameSuccess() {
		// Given
		User user = User.builder()
			.email("test@test.com")
			.password(encoder.encode("password"))
			.nickname("testNickname")
			.build();

		userJpaRepository.save(user);

		// When
		boolean exists = userRepository.existByNickname("testNickname");

		// Then
		assertThat(exists).isTrue();
	}

	@DisplayName("존재하지 않는 닉네임으로 조회 시 존재하지 않음을 반환한다.")
	@Test
	void existByNicknameNotFound() {
		// When
		boolean exists = userRepository.existByNickname("nonexistentNickname");

		// Then
		assertThat(exists).isFalse();
	}
}
