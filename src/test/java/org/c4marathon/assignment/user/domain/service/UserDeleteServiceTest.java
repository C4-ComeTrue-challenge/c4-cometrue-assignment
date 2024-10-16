package org.c4marathon.assignment.user.domain.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.c4marathon.assignment.user.domain.User;
import org.c4marathon.assignment.user.domain.repository.UserJpaRepository;
import org.c4marathon.assignment.user.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserDeleteServiceTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserJpaRepository userJpaRepository;

	@AfterEach
	void tearDown() {
		userJpaRepository.deleteAllInBatch();
	}

	@DisplayName("사용자를 성공적으로 삭제한다.")
	@Test
	void deleteUserSuccess() {
		// Given
		User user = User.builder()
			.email("test@test.com")
			.password("password")
			.nickname("testNickname")
			.build();

		user = userJpaRepository.save(user);

		// When
		userRepository.deleteUser(user);

		// Then
		Optional<User> deletedUser = userJpaRepository.findById(user.getId());
		assertThat(deletedUser).isNotPresent(); // 사용자가 삭제된 후 더 이상 존재하지 않아야 함
	}

	@DisplayName("존재하지 않는 사용자를 삭제하려고 할 때 예외가 발생하지 않지만 아무 일도 일어나지 않는다.")
	@Test
	void deleteNonExistingUser() {
		// Given
		User user = User.builder()
			.email("nonexistent@test.com")
			.password("password")
			.nickname("nonexistentNickname")
			.build();

		// When
		userRepository.deleteUser(user);

		// Then
		assertThat(userJpaRepository.findAll()).isEmpty();
	}
}
