package org.c4marathon.assignment.domin.user.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.domin.user.entity.User;
import org.c4marathon.assignment.domin.user.service.UserService;
import org.c4marathon.assignment.global.payload.ApiPayload;
import org.c4marathon.assignment.global.payload.CommonSuccessStatus;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @ResponseStatus(HttpStatus.CREATED)
    public ApiPayload<?> signup(@RequestBody signupRequestDTO signupRequestDTO) {
        userService.signup(signupRequestDTO);
        return ApiPayload.onSuccess(CommonSuccessStatus.CREATED, null);
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ApiPayload<?> login(@RequestBody loginRequestDTO loginRequestDTO, HttpSession httpSession) {
        User user = userService.login(loginRequestDTO);
        httpSession.setAttribute("user", user);
        return ApiPayload.onSuccess(CommonSuccessStatus.OK, null);
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ApiPayload<?> logout(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        httpSession.removeAttribute("user");
        return ApiPayload.onSuccess(CommonSuccessStatus.OK, null);
    }

    /**
     * 캐시 충전
     */
    @PostMapping("/charge")
    public ApiPayload<?> cacheCharge(@RequestBody CacheChargeRequestDTO chargeRequestDTO, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        userService.cacheCharge(chargeRequestDTO, user);
        return ApiPayload.onSuccess(CommonSuccessStatus.OK, null);
    }

}
