package org.c4marathon.assignment.user.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.global.exception.ErrorCode;
import org.c4marathon.assignment.user.domain.User;
import org.c4marathon.assignment.user.domain.repository.UserRepository;
import org.c4marathon.assignment.user.exception.DuplicateEmailException;
import org.c4marathon.assignment.user.exception.DuplicateNicknameException;
import org.c4marathon.assignment.user.exception.InvalidLoginException;
import org.c4marathon.assignment.user.exception.NotFountUserException;
import org.c4marathon.assignment.user.service.dto.UserLoginServiceDto;
import org.c4marathon.assignment.user.service.dto.UserRegisterServiceDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void register(UserRegisterServiceDto registerDto) {

        if (validateEmailDuplicate(registerDto.email())) {
            throw new DuplicateEmailException(ErrorCode.DUPLICATE_EMAIL);
        }
        if (validateNicknameDuplicate(registerDto.nickname())) {
            throw new DuplicateNicknameException(ErrorCode.DUPLICATE_NICKNAME);
        }

        User user = User.create(
                registerDto.email(),
                registerDto.nickname(),
                passwordEncoder.encode(registerDto.password())
        );
        userRepository.save(user);
    }

    public User login(UserLoginServiceDto loginDto) {
        User user = userRepository.findByEmail(loginDto.email())
                .orElseThrow(() -> new NotFountUserException(ErrorCode.NOT_FOUND_USER));

        if (!passwordEncoder.matches(loginDto.password(), user.getPassword())) {
            throw new InvalidLoginException(ErrorCode.INVALID_LOGIN);
        }
        return user;
    }

    private boolean validateNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    private boolean validateEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }
}
