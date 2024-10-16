package org.c4marathon.assignment.user.domain.repository;

import java.util.Optional;

import org.c4marathon.assignment.user.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<Users, Long> {
	boolean existsByEmail(String email);

	boolean existsByNickname(String nickname);

	Optional<Users> findByEmail(String email);
}
