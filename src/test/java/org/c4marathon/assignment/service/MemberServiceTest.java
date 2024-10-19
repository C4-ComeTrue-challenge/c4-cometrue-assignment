package org.c4marathon.assignment.service;

import org.c4marathon.assignment.domain.Member;
import org.c4marathon.assignment.domain.request.LoginRequest;
import org.c4marathon.assignment.domain.request.RegisterRequest;
import org.c4marathon.assignment.exception.LoginFailedException;
import org.c4marathon.assignment.exception.NicknameAlreadyExistsException;
import org.c4marathon.assignment.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // 회원가입 테스트: 성공
    @Test
    @DisplayName("회원가입 성공")
    void createMember_Success() {
        // given
        RegisterRequest request = new RegisterRequest("kumsh0330@naver.com", "1234", "sh");
        when(memberRepository.findByNickname("sh")).thenReturn(Optional.empty());
        // when
        memberService.createMember(request);

        // then
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 닉네임 중복")
    void createMember_NicknameAlreadyExists() {
        // given
        RegisterRequest request = new RegisterRequest("kumsh0330@naver.com", "1234", "sh");
        Member existingMember = Member.builder()
                .email("shkum0330@gmail.com")
                .password("1234")
                .nickname("sh")
                .build();

        when(memberRepository.findByNickname("sh")).thenReturn(Optional.of(existingMember));

        // when & then
        Exception exception = assertThrows(NicknameAlreadyExistsException.class, () -> memberService.createMember(request));

        // 예외 메시지 검증
        String expectedMessage = "이미 존재하는 닉네임입니다";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        verify(memberRepository, never()).save(any(Member.class));
    }

    // 로그인 테스트: 성공
    @Test
    @DisplayName("로그인 성공")
    void validateLogin_Success() {

    }

    // 로그인 테스트: 실패 (이메일 또는 비밀번호 불일치)
    @Test
    @DisplayName("로그인 실패")
    void validateLogin_Fail() {
        // given
        LoginRequest loginRequest = new LoginRequest("john@example.com", "wrongpassword");
        when(memberRepository.findByEmailAndPassword("john@example.com", "wrongpassword"))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(LoginFailedException.class, () -> memberService.validateLogin(loginRequest));
    }
}