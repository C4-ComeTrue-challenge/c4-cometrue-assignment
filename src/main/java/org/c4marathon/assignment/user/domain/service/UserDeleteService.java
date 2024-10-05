package org.c4marathon.assignment.user.domain.service;

import lombok.AllArgsConstructor;
import org.c4marathon.assignment.global.annotation.DomainService;
import org.c4marathon.assignment.user.domain.User;
import org.c4marathon.assignment.user.domain.repository.UserRepository;

@DomainService
@AllArgsConstructor
public class UserDeleteService {

    private final UserRepository userRepository;

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
