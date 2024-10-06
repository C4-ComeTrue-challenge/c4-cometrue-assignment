package org.c4marathon.assignment.user.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.user.domain.User;
import org.c4marathon.assignment.user.domain.service.UserDeleteService;
import org.c4marathon.assignment.user.domain.service.UserGetService;
import org.c4marathon.assignment.user.domain.service.UserSaveService;
import org.c4marathon.assignment.user.dto.EmailCheckResponse;
import org.c4marathon.assignment.user.dto.LoginRequest;
import org.c4marathon.assignment.user.dto.NicknameCheckResponse;
import org.c4marathon.assignment.user.dto.SignupRequest;
import org.c4marathon.assignment.user.exception.DuplicatedEmailException;
import org.c4marathon.assignment.user.exception.DuplicatedNicknameException;
import org.c4marathon.assignment.user.exception.WrongPasswordException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserSaveService saveService;
    private final UserGetService getService;
    private final UserDeleteService deleteService;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public void signup(SignupRequest request) {
        validateSignupRequest(request);
        User user = toUser(request);
        saveService.save(user);
    }

    @Transactional(readOnly = true)
    public User login(LoginRequest request) {
        User user = getService.getByEmail(request.email());
        validatePassword(request.password(), user.getPassword());
        return user;
    }

    @Transactional(readOnly = true)
    public EmailCheckResponse checkEmail(String email) {
        boolean exists = getService.existByEmail(email);
        return new EmailCheckResponse(exists);
    }

    @Transactional(readOnly = true)
    public NicknameCheckResponse checkNickname(String nickname) {
        boolean exists = getService.existByNickname(nickname);
        return new NicknameCheckResponse(exists);
    }

    @Transactional
    public void deleteUser(String email) {
        User user = getService.getByEmail(email);
        deleteService.deleteUser(user);
    }

    private void validateSignupRequest(SignupRequest request) {
        if (getService.existByEmail(request.email())) {
            throw new DuplicatedEmailException();
        }
        if (getService.existByNickname(request.nickname())) {
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
