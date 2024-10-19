package org.c4marathon.assignment.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.c4marathon.assignment.global.exception.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.c4marathon.assignment.user.domain.Users;
import org.c4marathon.assignment.user.domain.repository.UserJpaRepository;
import org.c4marathon.assignment.user.domain.repository.UserRepository;
import org.c4marathon.assignment.user.dto.EmailCheckResponse;
import org.c4marathon.assignment.user.dto.LoginRequest;
import org.c4marathon.assignment.user.dto.NicknameCheckResponse;
import org.c4marathon.assignment.user.dto.SignupRequest;
import org.c4marathon.assignment.user.exception.DuplicatedEmailException;
import org.c4marathon.assignment.user.exception.DuplicatedNicknameException;
import org.c4marathon.assignment.user.exception.WrongPasswordException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

	@MockBean
	private Clock clock;

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
	void signupFailForDuplicateEmail() {
		// Given
		userRepository.save(org.c4marathon.assignment.user.domain.Users.builder()
			.email("test@test.com")
			.password("password")
			.nickname("nickname")
			.build());

		SignupRequest request = new SignupRequest("test@test.com", "password", "testNickname");

		// When & Then
		assertThatThrownBy(() -> userService.signup(request)).isInstanceOf(DuplicatedEmailException.class)
			.hasMessageContaining(EMAIL_DUPLICATED_ERROR.getMessage());
	}

	@DisplayName("이미 존재하는 닉네임으로 회원가입 시 예외가 발생한다.")
	@Test
	void signupFailForDuplicateNickname() {
		// Given
		userRepository.save(org.c4marathon.assignment.user.domain.Users.builder()
			.email("test@test.com")
			.password("password")
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
			.password("password")
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
	void loginFailForWrongPassword() {
		// Given
		userRepository.save(org.c4marathon.assignment.user.domain.Users.builder()
			.email("test@test.com")
			.password("password")
			.nickname("testNickname")
			.build());

		LoginRequest request = new LoginRequest("test@test.com", "wrongPassword");

		// When & Then
		assertThatThrownBy(() -> userService.login(request)).isInstanceOf(WrongPasswordException.class)
			.hasMessageContaining(WRONG_PASSWORD_ERROR.getMessage());
	}

	@DisplayName("이메일 중복 여부를 확인한다.")
	@Test
	void checkEmailSuccess() {
		// Given
		userRepository.save(org.c4marathon.assignment.user.domain.Users.builder()
			.email("test@test.com")
			.password("password")
			.nickname("nickname")
			.build());

		// When
		EmailCheckResponse response = userService.checkEmail("test@test.com");

		// Then
		assertThat(response.isDuplicated()).isTrue();
	}

	@DisplayName("닉네임 중복 여부를 확인한다.")
	@Test
	void checkNicknameSuccess() {
		// Given
		userRepository.save(
			Users.builder().email("test@test.com").password("password").nickname("testNickname").build());

		// When
		NicknameCheckResponse response = userService.checkNickname("testNickname");

		// Then
		assertThat(response.isDuplicated()).isTrue();
	}

	@DisplayName("회원 탈퇴가 성공적으로 처리된다.")
	@Test
	void deleteUserSuccess() {
		// Given
		Users users = userRepository.save(
			Users.builder().email("test2@test.com").password("password").nickname("testNickname").build());

		LocalDateTime fixedTime = LocalDateTime.of(2024, 10, 19, 12, 0);
		Instant fixedInstant = fixedTime.toInstant(ZoneOffset.UTC);
		when(clock.instant()).thenReturn(fixedInstant);
		when(clock.getZone()).thenReturn(ZoneOffset.UTC);

		// When
		userService.deleteUser(users.getEmail(), "탈퇴하고 싶어서");

		// Then
		Users deletedUser = userRepository.getById(users.getId());
		assertTrue(deletedUser.isDeleted());
		assertEquals("탈퇴하고 싶어서", deletedUser.getDeletionReason());
		assertEquals(fixedTime, deletedUser.getDeletedDate());
	}
}
