package org.c4marathon.assignment.order.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.c4marathon.assignment.member.domain.Customer;
import org.c4marathon.assignment.product.domain.Product;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @Getter
    @Column(name = "order_id")
    private String orderId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private Long price;

    @Enumerated(STRING)
    private OrderState orderState;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private Order(
            String orderId,
            Customer customer,
            Product product,
            Long quantity,
            Long price,
            OrderState orderState
    ) {
        this.orderId = orderId;
        this.customer = customer;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.orderState = orderState;
    }

    public static Order of(
            String orderId,
            Customer customer,
            Product product,
            Long quantity,
            Long price,
            OrderState orderState) {
        return new Order(orderId, customer, product, quantity, price, orderState);
    }
}
