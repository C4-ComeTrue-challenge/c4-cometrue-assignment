package org.c4marathon.assignment.user.domain.repository;

import org.c4marathon.assignment.user.domain.Users;
import org.c4marathon.assignment.user.exception.NotFoundUserException;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepository {

	private final UserJpaRepository userJpaRepository;

	public Users getByEmail(String email) {
		return userJpaRepository.findByEmail(email)
			.orElseThrow(() -> new NotFoundUserException());
	}

	public Users getById(Long id) {
		return userJpaRepository.findById(id)
			.orElseThrow(() -> new NotFoundUserException());
	}

	public boolean existByEmail(String email) {
		return userJpaRepository.existsByEmail(email);
	}

	public boolean existByNickname(String nickname) {
		return userJpaRepository.existsByNickname(nickname);
	}

	public Users save(Users users) {
		return userJpaRepository.save(users);
	}
}
