package org.c4marathon.assignment.domin.order.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.domin.order.dto.OrderRequestDTO;
import org.c4marathon.assignment.domin.order.service.OrderService;
import org.c4marathon.assignment.domin.user.entity.User;
import org.c4marathon.assignment.global.payload.ApiPayload;
import org.c4marathon.assignment.global.payload.CommonSuccessStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.c4marathon.assignment.domin.order.dto.OrderRequestDTO.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ApiPayload<Long> createOrder(@RequestBody CreateOrderDTO createOrderDTO, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");

        orderService.createOrder(createOrderDTO, user.getId());
        return ApiPayload.onSuccess(CommonSuccessStatus.CREATED, null);
    }
}
