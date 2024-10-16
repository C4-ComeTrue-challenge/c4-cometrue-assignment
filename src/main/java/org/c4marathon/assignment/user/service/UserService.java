package org.c4marathon.assignment.user.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.c4marathon.assignment.user.domain.User;
import org.c4marathon.assignment.user.domain.repository.UserRepository;
import org.c4marathon.assignment.user.exception.*;
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
            throw new DuplicateEmailException();
        }
        if (validateNicknameDuplicate(registerDto.nickname())) {
            throw new DuplicateNicknameException();
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
                .orElseThrow(NotFountUserException::new);

        if (!loginDto.password().equals(user.getPassword())) {
            throw new InvalidLoginException();
        }
        return user.getId();
    }

    private boolean validateNicknameDuplicate(String nickname) {
        if (!validateNicknameFormat(nickname)) {
            throw new InvalidNicknameFormat();
        }
        return userRepository.existsByNickname(nickname);
    }

    private boolean validateEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean validateNicknameFormat(String nickname) {
        return StringUtils.isNotBlank(nickname) && !nickname.contains(" ");
    }

}
