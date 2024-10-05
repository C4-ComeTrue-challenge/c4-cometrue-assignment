package org.c4marathon.assignment.auth.domain;

import java.util.Optional;

import org.c4marathon.assignment.auth.domain.vo.UserType;

public interface UserRepository {
	Optional<User> findByEmail(String email);

	boolean supports(UserType type);
}
