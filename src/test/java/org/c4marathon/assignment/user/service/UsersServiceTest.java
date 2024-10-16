package org.c4marathon.assignment.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.c4marathon.assignment.global.exception.ErrorCode.*;

import org.c4marathon.assignment.user.domain.Users;
import org.c4marathon.assignment.user.domain.repository.UserJpaRepository;
import org.c4marathon.assignment.user.domain.repository.UserRepository;
import org.c4marathon.assignment.user.dto.EmailCheckResponse;
import org.c4marathon.assignment.user.dto.LoginRequest;
import org.c4marathon.assignment.user.dto.NicknameCheckResponse;
import org.c4marathon.assignment.user.dto.SignupRequest;
import org.c4marathon.assignment.user.exception.DuplicatedEmailException;
import org.c4marathon.assignment.user.exception.DuplicatedNicknameException;
import org.c4marathon.assignment.user.exception.NotFoundUserException;
import org.c4marathon.assignment.user.exception.WrongPasswordException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UsersServiceTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private UserJpaRepository userJpaRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@AfterEach
	void tearDown() {
		userJpaRepository.deleteAllInBatch();
	}

	@DisplayName("회원가입이 성공적으로 처리된다.")
	@Test
	void signupSuccess() {
		// Given
		SignupRequest request = new SignupRequest("test@test.com", "password", "testNickname");

		// When
		userService.signup(request);

		// Then
		Users savedUsers = userRepository.getByEmail("test@test.com");
		assertThat(savedUsers).isNotNull();
		assertThat(savedUsers.getEmail()).isEqualTo("test@test.com");
		assertThat(savedUsers.getNickname()).isEqualTo("testNickname");
	}

	@DisplayName("이미 존재하는 이메일로 회원가입 시 예외가 발생한다.")
	@Test
	void signupWithDuplicateEmail() {
		// Given
		userRepository.save(
			org.c4marathon.assignment.user.domain.Users.builder()
				.email("test@test.com")
				.password(encoder.encode("password"))
				.nickname("nickname")
				.build());

		SignupRequest request = new SignupRequest("test@test.com", "password", "testNickname");

		// When & Then
		assertThatThrownBy(() -> userService.signup(request)).isInstanceOf(DuplicatedEmailException.class)
			.hasMessageContaining(EMAIL_DUPLICATED_ERROR.getMessage());
	}

	@DisplayName("이미 존재하는 닉네임으로 회원가입 시 예외가 발생한다.")
	@Test
	void signupWithDuplicateNickname() {
		// Given
		userRepository.save(org.c4marathon.assignment.user.domain.Users.builder()
			.email("test@test.com")
			.password(encoder.encode("password"))
			.nickname("testNickname")
			.build());

		SignupRequest request = new SignupRequest("new@test.com", "password", "testNickname");

		// When & Then
		assertThatThrownBy(() -> userService.signup(request)).isInstanceOf(DuplicatedNicknameException.class)
			.hasMessageContaining(NICKNAME_DUPLICATED_ERROR.getMessage());
	}

	@DisplayName("로그인이 성공적으로 처리된다.")
	@Test
	void loginSuccess() {
		// Given
		userRepository.save(org.c4marathon.assignment.user.domain.Users.builder()
			.email("test@test.com")
			.password(encoder.encode("password"))
			.nickname("testNickname")
			.build());

		LoginRequest request = new LoginRequest("test@test.com", "password");

		// When
		Users loginUsers = userService.login(request);

		// Then
		assertThat(loginUsers.getEmail()).isEqualTo("test@test.com");
		assertThat(loginUsers.getNickname()).isEqualTo("testNickname");
	}

	@DisplayName("잘못된 비밀번호로 로그인 시 예외가 발생한다.")
	@Test
	void loginWithWrongPassword() {
		// Given
		userRepository.save(org.c4marathon.assignment.user.domain.Users.builder()
			.email("test@test.com")
			.password(encoder.encode("password"))
			.nickname("testNickname")
			.build());

		LoginRequest request = new LoginRequest("test@test.com", "wrongPassword");

		// When & Then
		assertThatThrownBy(() -> userService.login(request)).isInstanceOf(WrongPasswordException.class)
			.hasMessageContaining(WRONG_PASSWORD_ERROR.getMessage());
	}

	@DisplayName("이메일 중복 여부를 확인한다.")
	@Test
	void checkEmail() {
		// Given
		userRepository.save(
			org.c4marathon.assignment.user.domain.Users.builder()
				.email("test@test.com")
				.password(encoder.encode("password"))
				.nickname("nickname")
				.build());

		// When
		EmailCheckResponse response = userService.checkEmail("test@test.com");

		// Then
		assertThat(response.isDuplicated()).isTrue();
	}

	@DisplayName("닉네임 중복 여부를 확인한다.")
	@Test
	void checkNickname() {
		// Given
		userRepository.save(Users.builder()
			.email("test@test.com")
			.password(encoder.encode("password"))
			.nickname("testNickname")
			.build());

		// When
		NicknameCheckResponse response = userService.checkNickname("testNickname");

		// Then
		assertThat(response.isDuplicated()).isTrue();
	}

	@DisplayName("회원 탈퇴가 성공적으로 처리된다.")
	@Test
	void deleteUserSuccess() {
		// Given
		Users users = userRepository.save(org.c4marathon.assignment.user.domain.Users.builder()
			.email("test2@test.com")
			.password(encoder.encode("password"))
			.nickname("testNickname")
			.build());

		// When
		userService.deleteUser(users.getEmail());

		// Then
		assertThatThrownBy(() -> userRepository.getByEmail(users.getEmail())).isInstanceOf(NotFoundUserException.class)
			.hasMessageContaining(NOT_FOUND_USER_ERROR.getMessage());
	}
}
