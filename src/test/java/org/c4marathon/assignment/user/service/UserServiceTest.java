package org.c4marathon.assignment.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.c4marathon.assignment.global.exception.ErrorCode.*;

import org.c4marathon.assignment.user.domain.User;
import org.c4marathon.assignment.user.domain.repository.UserJpaRepository;
import org.c4marathon.assignment.user.domain.service.UserGetService;
import org.c4marathon.assignment.user.domain.service.UserSaveService;
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

@SpringBootTest
class UserServiceTest {

	@Autowired
	private UserSaveService userSaveService;

	@Autowired
	private UserGetService userGetService;

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
		User savedUser = userGetService.getByEmail("test@test.com");
		assertThat(savedUser).isNotNull();
		assertThat(savedUser.getEmail()).isEqualTo("test@test.com");
		assertThat(savedUser.getNickname()).isEqualTo("testNickname");
	}

	@DisplayName("이미 존재하는 이메일로 회원가입 시 예외가 발생한다.")
	@Test
	void signupWithDuplicateEmail() {
		// Given
		userSaveService.save(User.builder()
			.email("test@test.com")
			.password(encoder.encode("password"))
			.nickname("nickname")
			.build());

		SignupRequest request = new SignupRequest("test@test.com", "password", "testNickname");

		// When & Then
		assertThatThrownBy(() -> userService.signup(request))
			.isInstanceOf(DuplicatedEmailException.class)
			.hasMessageContaining(EMAIL_DUPLICATED_ERROR.getMessage());
	}

	@DisplayName("이미 존재하는 닉네임으로 회원가입 시 예외가 발생한다.")
	@Test
	void signupWithDuplicateNickname() {
		// Given
		userSaveService.save(User.builder()
			.email("test@test.com")
			.password(encoder.encode("password"))
			.nickname("testNickname")
			.build());

		SignupRequest request = new SignupRequest("new@test.com", "password", "testNickname");

		// When & Then
		assertThatThrownBy(() -> userService.signup(request))
			.isInstanceOf(DuplicatedNicknameException.class)
			.hasMessageContaining(NICKNAME_DUPLICATED_ERROR.getMessage());
	}

	@DisplayName("로그인이 성공적으로 처리된다.")
	@Test
	void loginSuccess() {
		// Given
		userSaveService.save(User.builder()
			.email("test@test.com")
			.password(encoder.encode("password"))
			.nickname("testNickname")
			.build());

		LoginRequest request = new LoginRequest("test@test.com", "password");

		// When
		User loginUser = userService.login(request);

		// Then
		assertThat(loginUser.getEmail()).isEqualTo("test@test.com");
		assertThat(loginUser.getNickname()).isEqualTo("testNickname");
	}

	@DisplayName("잘못된 비밀번호로 로그인 시 예외가 발생한다.")
	@Test
	void loginWithWrongPassword() {
		// Given
		userSaveService.save(User.builder()
			.email("test@test.com")
			.password(encoder.encode("password"))
			.nickname("testNickname")
			.build());

		LoginRequest request = new LoginRequest("test@test.com", "wrongPassword");

		// When & Then
		assertThatThrownBy(() -> userService.login(request))
			.isInstanceOf(WrongPasswordException.class)
			.hasMessageContaining(WRONG_PASSWORD_ERROR.getMessage());
	}

	@DisplayName("이메일 중복 여부를 확인한다.")
	@Test
	void checkEmail() {
		// Given
		userSaveService.save(User.builder()
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
		userSaveService.save(User.builder()
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
		User user = userSaveService.save(User.builder()
			.email("test2@test.com")
			.password(encoder.encode("password"))
			.nickname("testNickname")
			.build());

		// When
		userService.deleteUser(user.getEmail());

		// Then
		assertThatThrownBy(() -> userGetService.getByEmail(user.getEmail()))
			.isInstanceOf(NotFoundUserException.class)
			.hasMessageContaining(NOT_FOUND_USER_ERROR.getMessage());
	}
}
