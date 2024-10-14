package org.c4marathon.assignment.domin.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.c4marathon.assignment.domin.item.dto.ItemRequestDTO;
import org.c4marathon.assignment.domin.item.service.ItemService;
import org.c4marathon.assignment.domin.user.entity.Role;
import org.c4marathon.assignment.domin.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
    }

    @Test
    @DisplayName("상품 등록 성공")
    void registerItem_success() throws Exception {
        // Given
        ItemRequestDTO.ItemRegisterDTO itemRegisterDTO = new ItemRequestDTO.ItemRegisterDTO();
        itemRegisterDTO.setName("Test Item");
        itemRegisterDTO.setPrice(10000);

        User seller = User.builder()
                .role(Role.SELLER)
                .build();
        session.setAttribute("user", seller);

        // When & Then
        mockMvc.perform(post("/item/register")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRegisterDTO)))
                .andExpect(status().isCreated());

        Mockito.verify(itemService).register(any(ItemRequestDTO.ItemRegisterDTO.class), eq(seller));
    }

    @Test
    @DisplayName("상품 등록 실패 - Buyer 역할로 접근")
    void registerItem_fail_asBuyer() throws Exception {
        // Given
        ItemRequestDTO.ItemRegisterDTO itemRegisterDTO = new ItemRequestDTO.ItemRegisterDTO();
        itemRegisterDTO.setName("Test Item");
        itemRegisterDTO.setPrice(10000);

        User buyer = User.builder()
                .role(Role.BUYER)
                .build();
        session.setAttribute("user", buyer);

        // When & Then
        mockMvc.perform(post("/item/register")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRegisterDTO)))
                .andExpect(status().isBadRequest());

        Mockito.verify(itemService, Mockito.never()).register(any(ItemRequestDTO.ItemRegisterDTO.class), any(User.class));
    }

}