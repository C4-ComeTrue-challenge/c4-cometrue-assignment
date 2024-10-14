package org.c4marathon.assignment.domin.order.dto;

import lombok.Data;

import java.util.List;

public class OrderRequestDTO {

    @Data
    public static class CreateOrderDTO {
        private List<OrderItemDTO> orderItems;
    }

    @Data
    public static class OrderItemDTO {
        private Long itemId;
        private Integer quantity;
    }
}
