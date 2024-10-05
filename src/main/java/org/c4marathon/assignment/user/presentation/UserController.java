package org.c4marathon.assignment.user.presentation;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.global.util.SessionConst;
import org.c4marathon.assignment.user.domain.User;
import org.c4marathon.assignment.user.presentation.dto.UserLoginRequest;
import org.c4marathon.assignment.user.presentation.dto.UserRegisterRequest;
import org.c4marathon.assignment.user.presentation.dto.UserRegisterResponse;
import org.c4marathon.assignment.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/v1/register")
    public ResponseEntity<UserRegisterResponse> register(@Valid @RequestBody UserRegisterRequest registerDto) {

        UserRegisterResponse user = userService.register(registerDto.toServiceDto());

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/v1/login")
    public ResponseEntity<Void> login(@Valid @RequestBody UserLoginRequest loginDto, HttpServletRequest request) {
        User loginUser = userService.login(loginDto.toServiceDto());

        HttpSession session = request.getSession(true);
        session.setAttribute(SessionConst.LOGIN_USER, loginUser);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/v1/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok().build();
    }
}
