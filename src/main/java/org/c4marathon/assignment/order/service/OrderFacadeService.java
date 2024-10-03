package org.c4marathon.assignment.order.service;

import lombok.RequiredArgsConstructor;
import org.c4marathon.assignment.account.service.CommonAccountService;
import org.c4marathon.assignment.member.service.CustomerService;
import org.c4marathon.assignment.member.service.MerchantService;
import org.c4marathon.assignment.order.dto.OrderDto;
import org.c4marathon.assignment.product.domain.Product;
import org.c4marathon.assignment.product.service.OrderProductService;
import org.c4marathon.assignment.transaction.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderFacadeService {

    private final OrderService orderService;
    private final OrderProductService orderProductService;
    private final TransactionService transactionService;
    private final CustomerService customerService;
    private final MerchantService merchantService;
    private final CommonAccountService commonAccountService;

    @Transactional
    public void buyProduct(OrderDto dto) {
        Product product = orderProductService.findProductBy(dto.productId());
        Long totalPrice = product.getPrice() * dto.quantity();

        orderService.createOrder(dto.customerId(), dto.productId(), dto.quantity());
        product.decreaseStock(dto.quantity());
        transactionService.payForProduct(dto.customerId(), dto.merchantId(), totalPrice);
    }
}
