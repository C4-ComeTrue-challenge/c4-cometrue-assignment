package org.c4marathon.assignment.auth.domain;

import java.util.List;
import java.util.Optional;

import org.c4marathon.assignment.auth.domain.vo.UserType;
import org.springframework.stereotype.Repository;

@Repository
public class CompositeUserRepository {
	private final List<UserRepository> userRepositories;

	public CompositeUserRepository(List<UserRepository> userRepositories) {
		this.userRepositories = userRepositories;
	}

	public Optional<User> findByTypeAndEmail(UserType type, String email) {
		for (UserRepository userRepository : userRepositories) {
			if (userRepository.supports(type)) {
				return userRepository.findByEmail(email);
			}
		}
		return Optional.empty();
	}
}
