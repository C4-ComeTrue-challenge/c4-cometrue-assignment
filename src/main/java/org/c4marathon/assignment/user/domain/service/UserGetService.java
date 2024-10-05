package org.c4marathon.assignment.user.domain.service;

import lombok.AllArgsConstructor;
import org.c4marathon.assignment.global.annotation.DomainService;
import org.c4marathon.assignment.user.domain.User;
import org.c4marathon.assignment.user.domain.repository.UserRepository;
import org.c4marathon.assignment.user.exception.NotFoundUserException;

@DomainService
@AllArgsConstructor
public class UserGetService {

    private final UserRepository userRepository;

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundUserException());
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundUserException());
    }

    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

}
