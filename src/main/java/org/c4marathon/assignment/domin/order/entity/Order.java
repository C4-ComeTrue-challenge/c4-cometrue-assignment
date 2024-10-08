package org.c4marathon.assignment.domin.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.c4marathon.assignment.domin.user.entity.User;
import org.c4marathon.assignment.global.common.domain.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Status orderStatus;

    private Integer totalOrderPrice;

    @Builder
    public Order(User user, List<OrderItem> orderItems, Status orderStatus, Integer totalOrderPrice) {
        this.user = user;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
        this.totalOrderPrice = totalOrderPrice;
    }
}
