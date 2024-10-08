package org.c4marathon.assignment.domin.user.service;

import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

    @Transactional
    public void cacheCharge(UserRequestDTO.CacheChargeRequestDTO chargeRequestDTO, User user) {
        user.updateCache(chargeRequestDTO.getAmount());
        // 로그
        log.info("{} : {} 캐시 충전 완료", user.getId(), chargeRequestDTO.getAmount());
    }
}
