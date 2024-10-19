package org.c4marathon.assignment.user.presentation;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.c4marathon.assignment.config.CommonControllerTest;
import org.c4marathon.assignment.global.config.SessionConfig;
import org.c4marathon.assignment.user.domain.Users;
import org.c4marathon.assignment.user.dto.EmailCheckRequest;
import org.c4marathon.assignment.user.dto.EmailCheckResponse;
import org.c4marathon.assignment.user.dto.LoginRequest;
import org.c4marathon.assignment.user.dto.NicknameCheckRequest;
import org.c4marathon.assignment.user.dto.NicknameCheckResponse;
import org.c4marathon.assignment.user.dto.SignupRequest;
import org.c4marathon.assignment.user.dto.WithdrawRequest;
import org.c4marathon.assignment.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import jakarta.servlet.http.HttpServletRequest;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest extends CommonControllerTest {

	@MockBean
	private UserService userService;

	@MockBean
	private SessionConfig sessionConfig;

	@Test
	@DisplayName("POST /signup 요청 시 201 CREATED 상태를 반환해야 한다")
	void signupSuccess() throws Exception {
		// Given
		SignupRequest signupRequest = new SignupRequest("test@example.com", "password123", "testUser");

		mockMvc.perform(post("/api/user/signup").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(signupRequest))).andExpect(status().isCreated());

		Mockito.verify(userService, Mockito.times(1)).signup(any(SignupRequest.class));
	}

	@Test
	@DisplayName("POST /login 요청 시 200 OK 상태를 반환해야 한다")
	void loginSuccess() throws Exception {
		// Given
		LoginRequest loginRequest = new LoginRequest("test@example.com", "password123");
		Users mockUser = Users.builder().email("test@example.com").password("password123").nickname("testUser").build();

		Mockito.when(userService.login(any(LoginRequest.class))).thenReturn(mockUser);

		// When & Then
		mockMvc.perform(post("/api/user/login").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isOk());

		Mockito.verify(sessionConfig, Mockito.times(1)).createSession(any(HttpServletRequest.class), eq(mockUser));
	}

	@Test
	@DisplayName("POST /logout 요청 시 200 OK 상태를 반환해야 한다")
	void logoutSuccess() throws Exception {
		// When & Then
		mockMvc.perform(post("/api/user/logout")).andExpect(status().isOk());

		Mockito.verify(sessionConfig, Mockito.times(1)).invalidateSession(any(HttpServletRequest.class));
	}

	@Test
	@DisplayName("POST /check/nickname 요청 시 200 OK 상태와 닉네임 체크 결과를 반환해야 한다")
	void checkNicknameSuccess() throws Exception {
		// Given
		NicknameCheckRequest nicknameRequest = new NicknameCheckRequest("testUser");
		NicknameCheckResponse nicknameResponse = new NicknameCheckResponse(false);

		Mockito.when(userService.checkNickname(eq("testUser"))).thenReturn(nicknameResponse);

		// When & Then
		mockMvc.perform(post("/api/user/check/nickname").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(nicknameRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isDuplicated").value(false));

		Mockito.verify(userService, Mockito.times(1)).checkNickname(eq("testUser"));
	}

	@Test
	@DisplayName("POST /check/email 요청 시 200 OK 상태와 이메일 체크 결과를 반환해야 한다")
	void checkEmailSuccess() throws Exception {
		// Given
		EmailCheckRequest emailRequest = new EmailCheckRequest("test@example.com");
		EmailCheckResponse emailResponse = new EmailCheckResponse(false);

		Mockito.when(userService.checkEmail(eq("test@example.com"))).thenReturn(emailResponse);

		// When & Then
		mockMvc.perform(post("/api/user/check/email").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(emailRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.isDuplicated").value(false));

		Mockito.verify(userService, Mockito.times(1)).checkEmail(eq("test@example.com"));
	}

	@Test
	@DisplayName("POST /withdraw 요청 시 인증된 사용자라면 200 OK 상태를 반환해야 한다")
	void withdrawSuccess() throws Exception {
		// Given
		Users mockUser = Users.builder().email("test@example.com").password("password123").nickname("testUser").build();
		WithdrawRequest withdrawRequest = new WithdrawRequest("탈퇴하고 싶어서");

		Mockito.when(sessionConfig.getSessionUser(any(HttpServletRequest.class))).thenReturn(mockUser);

		// When & Then
		mockMvc.perform(post("/api/user/withdraw")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(withdrawRequest)))
			.andExpect(status().isOk());

		Mockito.verify(userService, Mockito.times(1))
			.deleteUser(eq(mockUser.getEmail()), eq(withdrawRequest.deletionReason()));
		Mockito.verify(sessionConfig, Mockito.times(1)).invalidateSession(any(HttpServletRequest.class));
	}

	@Test
	@DisplayName("POST /withdraw 요청 시 인증되지 않은 사용자라면 401 UNAUTHORIZED 상태를 반환해야 한다")
	void withdrawFailForInvalidUser() throws Exception {
		// Given
		Mockito.when(sessionConfig.getSessionUser(any(HttpServletRequest.class))).thenReturn(null);
		WithdrawRequest withdrawRequest = new WithdrawRequest("탈퇴하고 싶어서");

		// When & Then
		mockMvc.perform(post("/api/user/withdraw")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(withdrawRequest)))
			.andExpect(status().isUnauthorized());

		Mockito.verify(userService, Mockito.times(0)).deleteUser(any(String.class), any(String.class));
	}

	@Test
	@DisplayName("유효하지 않은 이메일로 회원가입 시 400 Bad Request")
	void signupFailForInvalidEmail() throws Exception {
		// Given
		SignupRequest signupRequest = new SignupRequest("invalid-email", "password123", "testUser");

		// When & Then
		mockMvc.perform(post("/api/user/signup").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(signupRequest))).andExpect(status().isBadRequest());

		Mockito.verify(userService, Mockito.times(0)).signup(any(SignupRequest.class));
	}

	@Test
	@DisplayName("유효하지 않은 이메일로 로그인 시 400 Bad Request")
	void loginFailForInvalidEmail() throws Exception {
		// Given
		LoginRequest loginRequest = new LoginRequest("invalid-email", "password123");

		// When & Then
		mockMvc.perform(post("/api/user/login").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(loginRequest))).andExpect(status().isBadRequest());

		Mockito.verify(userService, Mockito.times(0)).login(any(LoginRequest.class));
	}

	@Test
	@DisplayName("유효하지 않은 이메일 체크 시 400 Bad Request")
	void checkEmailFailForInvalidEmail() throws Exception {
		// Given
		EmailCheckRequest emailRequest = new EmailCheckRequest("invalid-email");

		// When & Then
		mockMvc.perform(post("/api/user/check/email").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(emailRequest))).andExpect(status().isBadRequest());

		Mockito.verify(userService, Mockito.times(0)).checkEmail(anyString());
	}

	@Test
	@DisplayName("빈 닉네임 체크 시 400 Bad Request")
	void checkEmailFailForEmptyNickname() throws Exception {
		// Given
		NicknameCheckRequest nicknameRequest = new NicknameCheckRequest("");

		// When & Then
		mockMvc.perform(post("/api/user/check/nickname").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(nicknameRequest))).andExpect(status().isBadRequest());

		Mockito.verify(userService, Mockito.times(0)).checkNickname(anyString());
	}
}
