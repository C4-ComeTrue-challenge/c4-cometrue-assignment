package org.c4marathon.assignment.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.domain.Member;
import org.c4marathon.assignment.domain.request.LoginRequest;
import org.c4marathon.assignment.domain.request.RegisterRequest;
import org.c4marathon.assignment.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        memberService.createMember(registerRequest);
        return ResponseEntity.ok("회원가입에 성공하였습니다.");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        memberService.validateLogin(loginRequest,session);
        return ResponseEntity.ok("로그인에 성공하였습니다.");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();  // 세션 무효화 (로그아웃 처리)
        return ResponseEntity.ok("로그아웃에 성공하였습니다.");
    }
    // todo: 회원정보 수정

    // todo: 회원탈퇴

}
