package org.c4marathon.assignment.domin.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.c4marathon.assignment.domin.user.dto.UserRequestDTO;
import org.c4marathon.assignment.domin.user.entity.User;
import org.c4marathon.assignment.domin.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signup() throws Exception {
        // Given
        UserRequestDTO.signupRequestDTO signupRequest = new UserRequestDTO.signupRequestDTO();
        signupRequest.setEmail("testUser@test.com");
        signupRequest.setPassword("testPassword");

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("로그인 성공")
    void login() throws Exception {
        // Given
        UserRequestDTO.loginRequestDTO loginRequest = new UserRequestDTO.loginRequestDTO();
        loginRequest.setEmail("testUser@test.com");
        loginRequest.setPassword("testPassword");

        User mockUser = User.builder()
                .email("testUser@test.com")
                .password("testPassword")
                .build();

        Mockito.when(userService.login(Mockito.any(UserRequestDTO.loginRequestDTO.class)))
                .thenReturn(mockUser);

        // When
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        HttpSession session = result.getRequest().getSession();
        User sessionUser = (User) session.getAttribute("user");
        assertThat(sessionUser.getEmail()).isEqualTo("testUser@test.com");
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logout() throws Exception {
        // Given
        User mockUser = User.builder()
                .email("testUser@test.com")
                .password("testPassword")
                .build();
        session.setAttribute("user", mockUser);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/user/logout")
                        .session(session))
                .andExpect(status().isOk());

        assertThat(session.getAttribute("user")).isNull();
    }

    @Test
    @DisplayName("캐시 충전 성공")
    void cacheCharge() throws Exception {
        // Given
        UserRequestDTO.CacheChargeRequestDTO chargeRequest = new UserRequestDTO.CacheChargeRequestDTO();
        chargeRequest.setAmount(1000);

        User mockUser = User.builder()
                .email("testUser@test.com")
                .password("testPassword")
                .build();
        session.setAttribute("user", mockUser);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/user/charge")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chargeRequest)))
                .andExpect(status().isOk());

        Mockito.verify(userService).cacheCharge(Mockito.any(UserRequestDTO.CacheChargeRequestDTO.class), Mockito.eq(mockUser));
    }

}