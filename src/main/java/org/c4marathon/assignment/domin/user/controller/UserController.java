package org.c4marathon.assignment.domin.user.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.domin.user.dto.UserRequestDTO;
import org.c4marathon.assignment.domin.user.entity.User;
import org.c4marathon.assignment.domin.user.repository.UserRepository;
import org.c4marathon.assignment.domin.user.service.UserService;
import org.c4marathon.assignment.global.payload.ApiPayload;
import org.c4marathon.assignment.global.payload.CommonSuccessStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.c4marathon.assignment.domin.user.dto.UserRequestDTO.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ApiPayload<?> signup(@RequestBody signupRequestDTO signupRequestDTO) {
        userService.signup(signupRequestDTO);
        return ApiPayload.onSuccess(CommonSuccessStatus.CREATED, null);
    }

    @PostMapping("/login")
    public ApiPayload<?> login(@RequestBody loginRequestDTO loginRequestDTO, HttpSession httpSession) {
        User user = userService.login(loginRequestDTO);
        httpSession.setAttribute("user", user);
        return ApiPayload.onSuccess(CommonSuccessStatus.OK, null);
    }

    @PostMapping("/logout")
    public ApiPayload<?> logout(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        httpSession.removeAttribute("user");
        return ApiPayload.onSuccess(CommonSuccessStatus.OK, null);
    }

}
