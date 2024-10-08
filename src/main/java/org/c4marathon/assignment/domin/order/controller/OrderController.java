package org.c4marathon.assignment.domin.order.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.domin.order.dto.OrderRequestDTO;
import org.c4marathon.assignment.domin.order.service.OrderService;
import org.c4marathon.assignment.domin.user.entity.User;
import org.c4marathon.assignment.global.payload.ApiPayload;
import org.c4marathon.assignment.global.payload.CommonSuccessStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.c4marathon.assignment.domin.order.dto.OrderRequestDTO.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;

    /**
     * 주문 생성
     */
    @PostMapping("/create")
    public ApiPayload<?> createOrder(@RequestBody CreateOrderDTO createOrderDTO, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");

        orderService.createOrder(createOrderDTO, user.getId());
        return ApiPayload.onSuccess(CommonSuccessStatus.CREATED, null);
    }

    /**
     * 주문 취소
     */
    @PatchMapping("/refund/{orderId}")
    public ApiPayload<?> refundOrder(@PathVariable("orderId") @Positive(message = "양수만 가능합니다") Long orderId, HttpSession httpSession){
        User user = (User) httpSession.getAttribute("user");

        orderService.refundOrder(orderId, user.getId());
        return ApiPayload.onSuccess(CommonSuccessStatus.OK, null);
    }

    /**
     * 구매 확정
     */
    @PostMapping("/confirm")
    public ApiPayload<?> confirmOrder(@PathVariable("orderId") @Positive(message = "양수만 가능합니다") Long orderId, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");

        orderService.confirmOrder(orderId);
        return ApiPayload.onSuccess(CommonSuccessStatus.OK, null);
    }
}
