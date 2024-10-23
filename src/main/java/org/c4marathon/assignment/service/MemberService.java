package org.c4marathon.assignment.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.domain.Member;
import org.c4marathon.assignment.domain.request.LoginRequest;
import org.c4marathon.assignment.domain.request.RegisterRequest;
import org.c4marathon.assignment.exception.LoginFailedException;
import org.c4marathon.assignment.exception.NicknameAlreadyExistsException;
import org.c4marathon.assignment.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public void createMember(RegisterRequest registerRequest) {
        if (memberRepository.findByNickname(registerRequest.getNickname()).isPresent()) {
            throw new NicknameAlreadyExistsException("이미 존재하는 닉네임입니다");
        }

        Member member = Member.builder()
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .nickname(registerRequest.getNickname())
                .build();

        memberRepository.save(member);
    }

    public void validateLogin(LoginRequest loginRequest, HttpSession session) {
        Member member = memberRepository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword())
                .orElseThrow(() -> new LoginFailedException("이메일 혹은 비밀번호를 확인해주세요"));
        session.setAttribute("member", member);  // 세션에 사용자 정보 저장
    }
}
