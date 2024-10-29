package org.c4marathon.assignment.user.domain.repository;

import static org.assertj.core.api.Assertions.*;
import static org.c4marathon.assignment.global.exception.ErrorCode.*;

import java.util.Optional;

import org.c4marathon.assignment.user.domain.Users;
import org.c4marathon.assignment.user.exception.NotFoundUserException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserJpaRepository userJpaRepository;

	@AfterEach
	void tearDown() {
		userJpaRepository.deleteAllInBatch();
	}

	@DisplayName("이메일로 사용자를 성공적으로 조회한다.")
	@Test
	void getByEmailSuccess() {
		// Given
		Users users = org.c4marathon.assignment.user.domain.Users.builder()
			.email("test@test.com")
			.password("password")
			.nickname("testNickname")
			.build();

		userJpaRepository.save(users);

		// When
		Users foundUsers = userRepository.getByEmail("test@test.com");

		// Then
		assertThat(foundUsers).isNotNull();
		assertThat(foundUsers.getEmail()).isEqualTo("test@test.com");
		assertThat(foundUsers.getNickname()).isEqualTo("testNickname");
	}

	@DisplayName("존재하지 않는 이메일로 조회 시 예외가 발생한다.")
	@Test
	void getByEmailFailForNotFound() {
		// When & Then
		assertThatThrownBy(() -> userRepository.getByEmail("notfound@test.com"))
			.isInstanceOf(NotFoundUserException.class)
			.hasMessageContaining(NOT_FOUND_USER_ERROR.getMessage());
	}

	@DisplayName("ID로 사용자를 성공적으로 조회한다.")
	@Test
	void getByIdSuccess() {
		// Given
		Users users = org.c4marathon.assignment.user.domain.Users.builder()
			.email("test@test.com")
			.password("password")
			.nickname("testNickname")
			.build();

		userJpaRepository.save(users);

		// When
		Users foundUsers = userRepository.getById(users.getId());

		// Then
		assertThat(foundUsers).isNotNull();
		assertThat(foundUsers.getId()).isEqualTo(users.getId());
		assertThat(foundUsers.getEmail()).isEqualTo("test@test.com");
	}

	@DisplayName("존재하지 않는 ID로 조회 시 예외가 발생한다.")
	@Test
	void getByIdFailForNotFound() {
		// When & Then
		assertThatThrownBy(() -> userRepository.getById(999L))
			.isInstanceOf(NotFoundUserException.class)
			.hasMessageContaining(NOT_FOUND_USER_ERROR.getMessage());
	}

	@DisplayName("이메일 존재 여부를 확인한다.")
	@Test
	void existByEmailSuccess() {
		// Given
		Users users = org.c4marathon.assignment.user.domain.Users.builder()
			.email("exists@test.com")
			.password("password")
			.nickname("testNickname")
			.build();

		userJpaRepository.save(users);

		// When
		boolean exists = userRepository.existByEmail("exists@test.com");

		// Then
		assertThat(exists).isTrue();
	}

	@DisplayName("존재하지 않는 이메일로 조회 시 존재하지 않음을 반환한다.")
	@Test
	void existByEmailFailForNotFound() {
		// When
		boolean exists = userRepository.existByEmail("notfound@test.com");

		// Then
		assertThat(exists).isFalse();
	}

	@DisplayName("닉네임 존재 여부를 확인한다.")
	@Test
	void existByNicknameSuccess() {
		// Given
		Users users = org.c4marathon.assignment.user.domain.Users.builder()
			.email("test@test.com")
			.password("password")
			.nickname("testNickname")
			.build();

		userJpaRepository.save(users);

		// When
		boolean exists = userRepository.existByNickname("testNickname");

		// Then
		assertThat(exists).isTrue();
	}

	@DisplayName("존재하지 않는 닉네임으로 조회 시 존재하지 않음을 반환한다.")
	@Test
	void existByNicknameFailForNotFound() {
		// When
		boolean exists = userRepository.existByNickname("nonexistentNickname");

		// Then
		assertThat(exists).isFalse();
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
	void saveUserFailForNullEmail() {
		// Given
		Users users = org.c4marathon.assignment.user.domain.Users.builder()
			.password("password")
			.nickname("testNickname")
			.build();
		// When & Then
		assertThatThrownBy(() -> userRepository.save(users)).isInstanceOf(DataIntegrityViolationException.class)
			.hasMessageContaining("could not execute statement");
	}

	@DisplayName("null 닉네임을 저장하려고 할 때 예외가 발생한다.")
	@Test
	void saveUserFailForNullNickname() {
		// Given
		Users users = org.c4marathon.assignment.user.domain.Users.builder()
			.email("test@test.com")
			.password("password")
			.build();

		// When & Then
		assertThatThrownBy(() -> userRepository.save(users)).isInstanceOf(DataIntegrityViolationException.class)
			.hasMessageContaining("could not execute statement");
	}

	@DisplayName("null 비밀번호를 저장하려고 할 때 예외가 발생한다.")
	@Test
	void saveUserFailForNullPassword() {
		// Given
		Users users = org.c4marathon.assignment.user.domain.Users.builder()
			.email("test@test.com")
			.nickname("testNickname")
			.build();

		// When & Then
		assertThatThrownBy(() -> userRepository.save(users)).isInstanceOf(DataIntegrityViolationException.class)
			.hasMessageContaining("could not execute statement");
	}
}
