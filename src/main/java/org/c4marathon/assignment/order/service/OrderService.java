package org.c4marathon.assignment.order.service;

import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.MEMBER_NOT_FOUND;
import static org.c4marathon.assignment.global.exception.exceptioncode.ExceptionCode.PRODUCT_NOT_FOUND;
import static org.c4marathon.assignment.order.domain.OrderState.PENDING;

import org.c4marathon.assignment.global.exception.AuthException;
import org.c4marathon.assignment.global.exception.ProductException;
import org.c4marathon.assignment.member.domain.Customer;
import org.c4marathon.assignment.member.domain.repository.CustomerRepository;
import org.c4marathon.assignment.order.domain.Orders;
import org.c4marathon.assignment.order.domain.repository.OrderRepository;
import org.c4marathon.assignment.order.util.OrderKeyGenerator;
import org.c4marathon.assignment.product.domain.Product;
import org.c4marathon.assignment.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderKeyGenerator orderKeyGenerator;

    @Transactional
    public void createOrder(Long customerId, Long productId, Long quantity) {
        Customer customer = customerRepository.findById(customerId)
                                              .orElseThrow(() -> new AuthException(MEMBER_NOT_FOUND));
        Product product = productRepository.findById(productId)
                                           .orElseThrow(() -> new ProductException(PRODUCT_NOT_FOUND));
        Orders newOrder = Orders.builder()
                              .orderId(orderKeyGenerator.generateOrderKey(customerId))
                              .customer(customer)
                              .product(product)
                              .price(product.getPrice())
                              .quantity(quantity)
                              .totalPrice(product.getPrice() * quantity)
                              .orderState(PENDING).build();

        orderRepository.save(newOrder);
    }
}
