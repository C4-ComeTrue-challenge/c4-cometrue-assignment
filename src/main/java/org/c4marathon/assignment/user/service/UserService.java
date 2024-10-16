package org.c4marathon.assignment.user.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.global.exception.ErrorCode;
import org.c4marathon.assignment.user.domain.User;
import org.c4marathon.assignment.user.domain.repository.UserRepository;
import org.c4marathon.assignment.user.exception.DuplicateEmailException;
import org.c4marathon.assignment.user.exception.DuplicateNicknameException;
import org.c4marathon.assignment.user.exception.InvalidLoginException;
import org.c4marathon.assignment.user.exception.NotFountUserException;
import org.c4marathon.assignment.user.presentation.dto.UserRegisterResponse;
import org.c4marathon.assignment.user.service.dto.UserLoginServiceRequest;
import org.c4marathon.assignment.user.service.dto.UserRegisterServiceRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserRegisterResponse register(UserRegisterServiceRequest registerDto) {

        if (validateEmailDuplicate(registerDto.email())) {
            throw new DuplicateEmailException(ErrorCode.DUPLICATE_EMAIL);
        }
        if (validateNicknameDuplicate(registerDto.nickname())) {
            throw new DuplicateNicknameException(ErrorCode.DUPLICATE_NICKNAME);
        }

        User user = User.create(
                registerDto.email(),
                registerDto.nickname(),
                registerDto.password()
        );
        userRepository.save(user);
        return new UserRegisterResponse(user.getId(), user.getEmail(), user.getNickname());
    }


    public Long login(UserLoginServiceRequest loginDto) {
        User user = userRepository.findByEmail(loginDto.email())
                .orElseThrow(() -> new NotFountUserException(ErrorCode.NOT_FOUND_USER));

        if (!loginDto.password().equals(user.getPassword())) {
            throw new InvalidLoginException(ErrorCode.INVALID_LOGIN);
        }
        return user.getId();
    }

    private boolean validateNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    private boolean validateEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }
}
