package org.c4marathon.assignment.user.domain.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.c4marathon.assignment.user.domain.Users;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UsersJpaRepositoryTest {

	@Autowired
	private UserJpaRepository userJpaRepository;

	@AfterEach
	void tearDown() {
		userJpaRepository.deleteAllInBatch();
	}

	@DisplayName("이메일로 사용자를 성공적으로 조회한다.")
	@Test
	void findByEmailSuccess() {
		// Given
		Users users = org.c4marathon.assignment.user.domain.Users.builder()
			.email("test@test.com")
			.password("password")
			.nickname("testNickname")
			.build();

		userJpaRepository.save(users);

		// When
		Optional<Users> foundUser = userJpaRepository.findByEmail("test@test.com");

		// Then
		assertThat(foundUser).isPresent();
		assertThat(foundUser.get().getEmail()).isEqualTo("test@test.com");
		assertThat(foundUser.get().getNickname()).isEqualTo("testNickname");
	}

	@DisplayName("존재하지 않는 이메일로 조회 시 빈 결과를 반환한다.")
	@Test
	void findByEmailNotFound() {
		// When
		Optional<Users> foundUser = userJpaRepository.findByEmail("notfound@test.com");

		// Then
		assertThat(foundUser).isNotPresent();
	}

	@DisplayName("이메일 존재 여부를 확인한다.")
	@Test
	void existsByEmailSuccess() {
		// Given
		Users users = org.c4marathon.assignment.user.domain.Users.builder()
			.email("exists@test.com")
			.password("password")
			.nickname("testNickname")
			.build();

		userJpaRepository.save(users);

		// When
		boolean exists = userJpaRepository.existsByEmail("exists@test.com");

		// Then
		assertThat(exists).isTrue();
	}

	@DisplayName("존재하지 않는 이메일로 존재 여부를 확인하면 false를 반환한다.")
	@Test
	void existsByEmailNotFound() {
		// When
		boolean exists = userJpaRepository.existsByEmail("notfound@test.com");

		// Then
		assertThat(exists).isFalse();
	}

	@DisplayName("닉네임 존재 여부를 확인한다.")
	@Test
	void existsByNicknameSuccess() {
		// Given
		Users users = org.c4marathon.assignment.user.domain.Users.builder()
			.email("test@test.com")
			.password("password")
			.nickname("testNickname")
			.build();

		userJpaRepository.save(users);

		// When
		boolean exists = userJpaRepository.existsByNickname("testNickname");

		// Then
		assertThat(exists).isTrue();
	}

	@DisplayName("존재하지 않는 닉네임으로 존재 여부를 확인하면 false를 반환한다.")
	@Test
	void existsByNicknameNotFound() {
		// When
		boolean exists = userJpaRepository.existsByNickname("nonexistentNickname");

		// Then
		assertThat(exists).isFalse();
	}
}
