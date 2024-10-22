package org.c4marathon.assignment.order.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.*;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import org.c4marathon.assignment.member.domain.Customer;
import org.c4marathon.assignment.product.domain.Product;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Orders {

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
    private Long price;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private Long totalPrice;

    @Enumerated(STRING)
    private OrderState orderState;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Builder
    private Orders(
            String orderId,
            Customer customer,
            Product product,
            Long price,
            Long quantity,
            Long totalPrice,
            OrderState orderState
    ) {
        this.orderId = orderId;
        this.customer = customer;
        this.product = product;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderState = orderState;
    }

    public static Orders of(
            String orderId,
            Customer customer,
            Product product,
            Long price,
            Long quantity,
            Long totalPrice,
            OrderState orderState) {
        return new Orders(orderId, customer, product, price, quantity, totalPrice, orderState);
    }
}
