package org.c4marathon.assignment.domin.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.c4marathon.assignment.domin.order.dto.OrderRequestDTO;
import org.c4marathon.assignment.domin.order.service.OrderService;
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

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
    }

    @Test
    @DisplayName("주문 생성 성공")
    void createOrder_success() throws Exception {
        // Given
        OrderRequestDTO.CreateOrderDTO createOrderDTO = new OrderRequestDTO.CreateOrderDTO();
        OrderRequestDTO.OrderItemDTO orderItemDTO = new OrderRequestDTO.OrderItemDTO();
        orderItemDTO.setItemId(1L);
        orderItemDTO.setQuantity(2);

        createOrderDTO.setOrderItems(Arrays.asList(orderItemDTO));

        User buyer = User.builder()
                .id(1L)
                .build();
        session.setAttribute("user", buyer);

        // When & Then
        mockMvc.perform(post("/order/create")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrderDTO)))
                .andExpect(status().isCreated());

        Mockito.verify(orderService).createOrder(eq(createOrderDTO), eq(buyer.getId()));
    }

    @Test
    @DisplayName("주문 취소 성공")
    void refundOrder_success() throws Exception {
        // Given
        Long orderId = 1L;

        User buyer = User.builder()
                .id(1L)
                .build();
        session.setAttribute("user", buyer);

        // When & Then
        mockMvc.perform(patch("/order/refund/{orderId}", orderId)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(orderService).refundOrder(eq(orderId), eq(buyer.getId()));
    }

    @Test
    @DisplayName("주문 확정 성공")
    void confirmOrder_success() throws Exception {
        // Given
        Long orderId = 1L;

        User buyer = User.builder()
                .id(1L)
                .build();
        session.setAttribute("user", buyer);

        // When & Then
        mockMvc.perform(post("/order/confirm/{orderId}", orderId)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(orderService).confirmOrder(eq(orderId));
    }

}