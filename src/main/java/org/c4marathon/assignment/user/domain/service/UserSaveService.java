package org.c4marathon.assignment.user.domain.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.global.annotation.DomainService;
import org.c4marathon.assignment.user.domain.User;
import org.c4marathon.assignment.user.domain.repository.UserRepository;

@DomainService
@RequiredArgsConstructor
public class UserSaveService {

    private final UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }
}