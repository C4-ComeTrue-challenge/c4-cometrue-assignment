package org.c4marathon.assignment.domin.user.service;


import static org.junit.jupiter.api.Assertions.*;

import org.c4marathon.assignment.domin.user.controller.UserErrorStatus;
import org.c4marathon.assignment.domin.user.dto.UserRequestDTO;
import org.c4marathon.assignment.domin.user.entity.Role;
import org.c4marathon.assignment.domin.user.entity.User;
import org.c4marathon.assignment.domin.user.repository.UserRepository;
import org.c4marathon.assignment.global.exception.GeneralException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserRequestDTO.CacheChargeRequestDTO chargeRequestDTO;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class 회원가입_테스트 {
        @Test
        void 회원가입_성공() {
            // Given
            UserRequestDTO.signupRequestDTO signupRequestDTO = new UserRequestDTO.signupRequestDTO();
            signupRequestDTO.setEmail("test@example.com");
            signupRequestDTO.setPassword("password");
            signupRequestDTO.setName("Test User");
            signupRequestDTO.setRole(Role.BUYER);
            signupRequestDTO.setAccount("1234567890");
            signupRequestDTO.setBank("Test Bank");

            // When
            userService.signup(signupRequestDTO);

            // Then
            verify(userRepository, times(1)).save(any(User.class));
        }
    }

    @Nested
    class 로그인_테스트 {
        @Test
        void 로그인_성공() {
            // Given
            UserRequestDTO.loginRequestDTO loginRequestDTO = new UserRequestDTO.loginRequestDTO();
            loginRequestDTO.setEmail("test@example.com");
            loginRequestDTO.setPassword("password");

            User mockUser = User.builder()
                    .email("test@example.com")
                    .password("password")
                    .build();

            when(userRepository.findByEmailAndPassword(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()))
                    .thenReturn(Optional.of(mockUser));

            // When
            User result = userService.login(loginRequestDTO);

            // Then
            assertNotNull(result);
            assertEquals("test@example.com", result.getEmail());
            assertEquals("password", result.getPassword());
        }

        @Test
        void 로그인_실패() {
            // Given
            UserRequestDTO.loginRequestDTO loginRequestDTO = new UserRequestDTO.loginRequestDTO();
            loginRequestDTO.setEmail("test@example.com");
            loginRequestDTO.setPassword("wrong_password");

            when(userRepository.findByEmailAndPassword(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()))
                    .thenReturn(Optional.empty());

            // When & Then
            GeneralException exception = assertThrows(GeneralException.class, () -> userService.login(loginRequestDTO));
            assertEquals(UserErrorStatus.USER_INFO_NOT_FOUND, exception.getCode());
        }

    }

    @Nested
    class 캐시_충전_테스트 {
        @Test
        void 캐시_충전_성공() {
            user = mock(User.class);
            chargeRequestDTO = new UserRequestDTO.CacheChargeRequestDTO();
            chargeRequestDTO.setAmount(5000);

            userService.cacheCharge(chargeRequestDTO, user);

            verify(user, times(1)).updateCache(chargeRequestDTO.getAmount());
        }
    }
}