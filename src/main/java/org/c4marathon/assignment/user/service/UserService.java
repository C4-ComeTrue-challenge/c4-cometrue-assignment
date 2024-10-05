package org.c4marathon.assignment.user.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.user.domain.User;
import org.c4marathon.assignment.user.domain.service.UserDeleteService;
import org.c4marathon.assignment.user.domain.service.UserGetService;
import org.c4marathon.assignment.user.domain.service.UserSaveService;
import org.c4marathon.assignment.user.dto.*;
import org.c4marathon.assignment.user.exception.DuplicatedEmailException;
import org.c4marathon.assignment.user.exception.DuplicatedNicknameException;
import org.c4marathon.assignment.user.exception.WrongPasswordException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static org.c4marathon.assignment.user.service.mapper.UserMapper.toUser;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserSaveService saveService;
    private final UserGetService getService;
    private final UserDeleteService deleteService;
    private final BCryptPasswordEncoder encoder;

    public SignupResponse signup(SignupRequest request) {
        validateSignupRequest(request);
        User user = createUser(request);
        return new SignupResponse(saveService.save(user).getId());
    }

    public User login(LoginRequest request) {
        User user = getService.getByEmail(request.email());
        validatePassword(request.password(), user.getPassword());
        return user;
    }

    public EmailCheckResponse checkEmail(String email) {
        boolean exists = getService.existByEmail(email);
        return new EmailCheckResponse(exists);
    }

    public NicknameCheckResponse checkNickname(String nickname) {
        boolean exists = getService.existByNickname(nickname);
        return new NicknameCheckResponse(exists);
    }

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

    private User createUser(SignupRequest request) {
        return toUser(
                request.email(),
                encoder.encode(request.password()),
                request.nickname()
        );
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!encoder.matches(rawPassword, encodedPassword)) {
            throw new WrongPasswordException();
        }
    }
}
