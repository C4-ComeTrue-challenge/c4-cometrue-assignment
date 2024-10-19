package org.c4marathon.assignment.user.presentation;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/user")
@RequiredArgsConstructor
@RestController
public class UserController {
	private final UserService userService;
	private final SessionConfig sessionConfig;

	@PostMapping("/signup")
	public ResponseEntity<Void> signup(
		@Valid @RequestBody SignupRequest request
	) {
		userService.signup(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/login")
	public ResponseEntity<Void> login(
		@Valid @RequestBody LoginRequest request,
		HttpServletRequest httpServletRequest
	) {
		Users users = userService.login(request);
		sessionConfig.createSession(httpServletRequest, users);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(
		HttpServletRequest httpServletRequest
	) {
		sessionConfig.invalidateSession(httpServletRequest);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/check/nickname")
	public ResponseEntity<NicknameCheckResponse> checkNickname(
		@Valid @RequestBody NicknameCheckRequest request
	) {
		NicknameCheckResponse response = userService.checkNickname(request.nickname());
		return ResponseEntity.ok(response);
	}

	@PostMapping("/check/email")
	public ResponseEntity<EmailCheckResponse> checkEmail(
		@Valid @RequestBody EmailCheckRequest request
	) {
		EmailCheckResponse response = userService.checkEmail(request.email());
		return ResponseEntity.ok(response);
	}

	@PostMapping("/withdraw")
	public ResponseEntity<Void> deleteUser(
		@RequestBody WithdrawRequest request,
		HttpServletRequest httpServletRequest
	) {
		Users loginUsers = sessionConfig.getSessionUser(httpServletRequest);

		if (loginUsers == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		userService.deleteUser(loginUsers.getEmail(), request.deletionReason());
		sessionConfig.invalidateSession(httpServletRequest);

		return ResponseEntity.ok().build();
	}
}
