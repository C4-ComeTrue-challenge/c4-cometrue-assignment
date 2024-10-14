package org.c4marathon.assignment.order.service;

import org.c4marathon.assignment.order.dto.OrderDto;
import org.c4marathon.assignment.product.domain.Product;
import org.c4marathon.assignment.product.service.OrderProductService;
import org.c4marathon.assignment.transaction.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderFacadeService {

    private final OrderService orderService;
    private final OrderProductService orderProductService;
    private final TransactionService transactionService;

    @Transactional
    public void buyProduct(OrderDto dto) {
        Product product = orderProductService.findProductBy(dto.productId());
        Long totalPrice = product.getPrice() * dto.quantity();

        orderService.createOrder(dto.customerId(), dto.productId(), dto.quantity());
        product.decreaseStock(dto.quantity());
        transactionService.payForProduct(dto.customerId(), dto.merchantId(), totalPrice);
    }
}
