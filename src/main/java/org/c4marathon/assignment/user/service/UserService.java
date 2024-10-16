package org.c4marathon.assignment.user.service;

import org.c4marathon.assignment.user.domain.User;
import org.c4marathon.assignment.user.domain.repository.UserRepository;
import org.c4marathon.assignment.user.dto.EmailCheckResponse;
import org.c4marathon.assignment.user.dto.LoginRequest;
import org.c4marathon.assignment.user.dto.NicknameCheckResponse;
import org.c4marathon.assignment.user.dto.SignupRequest;
import org.c4marathon.assignment.user.exception.DuplicatedEmailException;
import org.c4marathon.assignment.user.exception.DuplicatedNicknameException;
import org.c4marathon.assignment.user.exception.WrongPasswordException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder encoder;

	@Transactional
	public void signup(SignupRequest request) {
		validateSignupRequest(request);
		User user = toUser(request);
		userRepository.save(user);
	}

	@Transactional(readOnly = true)
	public User login(LoginRequest request) {
		User user = userRepository.getByEmail(request.email());
		validatePassword(request.password(), user.getPassword());
		return user;
	}

	@Transactional(readOnly = true)
	public EmailCheckResponse checkEmail(String email) {
		boolean exists = userRepository.existByEmail(email);
		return new EmailCheckResponse(exists);
	}

	@Transactional(readOnly = true)
	public NicknameCheckResponse checkNickname(String nickname) {
		boolean exists = userRepository.existByNickname(nickname);
		return new NicknameCheckResponse(exists);
	}

	@Transactional
	public void deleteUser(String email) {
		User user = userRepository.getByEmail(email);
		userRepository.deleteUser(user);
	}

	private void validateSignupRequest(SignupRequest request) {
		if (userRepository.existByEmail(request.email())) {
			throw new DuplicatedEmailException();
		}
		if (userRepository.existByNickname(request.nickname())) {
			throw new DuplicatedNicknameException();
		}
	}

	private User toUser(SignupRequest request) {
		return User.builder()
			.email(request.email())
			.password(encoder.encode(request.password()))
			.nickname(request.nickname())
			.build();
	}

	private void validatePassword(String rawPassword, String encodedPassword) {
		if (!encoder.matches(rawPassword, encodedPassword)) {
			throw new WrongPasswordException();
		}
	}
}
