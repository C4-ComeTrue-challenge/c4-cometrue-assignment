package org.c4marathon.assignment.domin.user.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.domin.user.controller.UserErrorStatus;
import org.c4marathon.assignment.domin.user.dto.UserRequestDTO;
import org.c4marathon.assignment.domin.user.entity.User;
import org.c4marathon.assignment.domin.user.repository.UserRepository;
import org.c4marathon.assignment.global.exception.GeneralException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void signup(UserRequestDTO.signupRequestDTO signupRequestDTO) {
        User user = User.builder()
                .email(signupRequestDTO.getEmail())
                .password(signupRequestDTO.getPassword())
                .name(signupRequestDTO.getName())
                .role(signupRequestDTO.getRole())
                .account(signupRequestDTO.getAccount())
                .bank(signupRequestDTO.getBank())
                .build();

        userRepository.save(user);
    }

    public User login(UserRequestDTO.loginRequestDTO loginRequestDTO) {
        return userRepository.findByEmailAndPassword(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
                .orElseThrow(() -> new GeneralException(UserErrorStatus.USER_INFO_NOT_FOUND));
    }
}
