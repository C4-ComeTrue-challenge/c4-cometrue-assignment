package org.c4marathon.assignment.domin.order.entity;

import jakarta.persistence.*;
import lombok.*;
import org.c4marathon.assignment.domin.user.entity.User;
import org.c4marathon.assignment.global.common.domain.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Status orderStatus;

    private Integer totalOrderPrice;

    public void updateStatus(Status newStatus) {
        this.orderStatus = newStatus;
    }
}
