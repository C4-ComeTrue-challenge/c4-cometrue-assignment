package org.c4marathon.assignment.user.service;

import org.c4marathon.assignment.user.domain.User;
import org.c4marathon.assignment.user.domain.repository.UserRepository;
import org.c4marathon.assignment.user.exception.DuplicateEmailException;
import org.c4marathon.assignment.user.exception.DuplicateNicknameException;
import org.c4marathon.assignment.user.exception.InvalidLoginException;
import org.c4marathon.assignment.user.exception.NotFountUserException;
import org.c4marathon.assignment.user.service.dto.UserLoginServiceDto;
import org.c4marathon.assignment.user.service.dto.UserRegisterServiceDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @DisplayName("회웝가입 테스트")
    @Test
    void register() {
        // given
        UserRegisterServiceDto registerDto = new UserRegisterServiceDto(
                "test@test.com",
                "1234",
                "opix"
        );
        // when
        Long register = userService.register(registerDto);

        // then
        assertThat(register).isNotNull();

    }

    @DisplayName("가입되어있는 이메일로 회원가입을 할 경우 예외가 발생한다.")
    @Test
    void registerByDuplicateEmail() {
        // given
        User user = User.create("test@test.com", "1234", "test");
        userRepository.save(user);

        UserRegisterServiceDto registerDto = new UserRegisterServiceDto(
                "test@test.com",
                "1234",
                "opix"
        );
        // when // then
        assertThatThrownBy(() -> userService.register(registerDto))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessage("이미 가입한 이메일 입니다.");
    }

    @DisplayName("가입되어있는 닉네임으로 회원가입을 할 경우 예외가 발생한다.")
    @Test
    void registerByDuplicateNickname() {
        // given
        User user = User.create("test@test.com", "1234", "test");
        userRepository.save(user);

        UserRegisterServiceDto registerDto = new UserRegisterServiceDto(
                "test1@test.com",
                "1234",
                "test"
        );
        // when // then
        assertThatThrownBy(() -> userService.register(registerDto))
                .isInstanceOf(DuplicateNicknameException.class)
                .hasMessage("중복된 닉네임 입니다.");
    }

    @DisplayName("로그인 테스트")
    @Test
    void login() {
        // given
        User user = User.create("test@test.com", "1234", "test");
        userRepository.save(user);

        UserLoginServiceDto loginDto = new UserLoginServiceDto("test@test.com", "1234");

        // when
        User loginUser = userService.login(loginDto);

        // then
        assertThat(loginUser)
                .extracting("email", "nickname")
                .contains("test@test.com", "test");
    }

    @DisplayName("가입한적 없는 이메일로 로그인 시도를 할 경우 예외가 발생한다.")
    @Test
    void loginWithNonExistentEmail() {
        // given
        User user = User.create("test@test.com", "1234", "test");
        userRepository.save(user);

        UserLoginServiceDto loginDto = new UserLoginServiceDto("test1@test.com", "1234");

        // when // then
        assertThatThrownBy(() -> userService.login(loginDto))
                .isInstanceOf(NotFountUserException.class)
                .hasMessage("조회된 유저가 없습니다.");
    }

    @DisplayName("틀린 비밀번호로 로그인 시도할 경우 예외가 발생한다.")
    @Test
    void loginWithIncorrectPassword() {
        // given
        User user = User.create("test@test.com", "1234", "test");
        userRepository.save(user);

        UserLoginServiceDto loginDto = new UserLoginServiceDto("test@test.com", "12345");

        // when // then
        assertThatThrownBy(() -> userService.login(loginDto))
                .isInstanceOf(InvalidLoginException.class)
                .hasMessage("잘못된 자격 증명입니다.");
    }

}