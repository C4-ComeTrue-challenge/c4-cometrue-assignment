package org.c4marathon.assignment.user.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.c4marathon.assignment.ControllerTestSupport;
import org.c4marathon.assignment.global.util.SessionConst;
import org.c4marathon.assignment.user.domain.User;
import org.c4marathon.assignment.user.presentation.dto.UserLoginRequest;
import org.c4marathon.assignment.user.presentation.dto.UserRegisterRequest;
import org.c4marathon.assignment.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("회원가입 성공")
    void register() throws Exception {
        // given
        UserRegisterRequest registerDto = new UserRegisterRequest("test@test.com", "1234", "test");


        // when //then
        mockMvc.perform(
                post("/api/v1/register")
                        .content(objectMapper.writeValueAsString(registerDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("로그인 성공")
    @Test
    void login() throws Exception{
        // given
        UserLoginRequest loginDto = new UserLoginRequest("test@test.com", "1234");

        User user = User.create("test@test.com", "1234", "test");
        given(userService.login(any())).willReturn(user);

        // when // then
        mockMvc.perform(
                        post("/api/v1/login")
                                .content(objectMapper.writeValueAsString(loginDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(request().sessionAttribute(SessionConst.LOGIN_USER, user));
    }

    @DisplayName("로그아웃 성공")
    @Test
    void logoutSuccess() throws Exception {
        // given
        MockHttpSession session = new MockHttpSession();

        User user = User.create("test@test.com", "1234", "test");
        session.setAttribute(SessionConst.LOGIN_USER, user);

        // when // then
        mockMvc.perform(
                post("/api/v1/logout")
                        .session(session)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(request -> assertNull(request.getRequest().getSession(false)));
    }

}