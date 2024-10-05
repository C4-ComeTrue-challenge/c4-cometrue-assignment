package org.c4marathon.assignment.user.domain.service;

import lombok.AllArgsConstructor;
import org.c4marathon.assignment.global.annotation.DomainService;
import org.c4marathon.assignment.user.domain.User;
import org.c4marathon.assignment.user.domain.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@AllArgsConstructor
public class UserSaveService {

    private final UserRepository userRepository;

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }
}