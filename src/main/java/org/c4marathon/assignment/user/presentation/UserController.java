package org.c4marathon.assignment.user.presentation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.global.config.SessionConfig;
import org.c4marathon.assignment.user.domain.User;
import org.c4marathon.assignment.user.dto.*;
import org.c4marathon.assignment.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/user")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private final SessionConfig sessionConfig;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(
            @Valid @RequestBody SignupRequest request
    ) {
        SignupResponse response = userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpServletRequest
    ) {
        User user = userService.login(request);
        sessionConfig.createSession(httpServletRequest, user);
        return ResponseEntity.ok(new LoginResponse(user.getId()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest httpServletRequest
    ) {
        sessionConfig.invalidateSession(httpServletRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/check/nickname")
    public ResponseEntity<NicknameCheckResponse> checkNickname(
            @Valid @RequestBody NicknameCheckRequest request
    ) {
        NicknameCheckResponse response = userService.checkNickname(request.nickname());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/check/email")
    public ResponseEntity<EmailCheckResponse> checkEmail(
            @Valid @RequestBody EmailCheckRequest request
    ) {
        EmailCheckResponse response = userService.checkEmail(request.email());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> deleteUser(
            HttpServletRequest httpServletRequest
    ) {
        User loginUser = sessionConfig.getSessionUser(httpServletRequest);

        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userService.deleteUser(loginUser.getEmail());
        sessionConfig.invalidateSession(httpServletRequest);

        return ResponseEntity.ok().build();
    }
}
