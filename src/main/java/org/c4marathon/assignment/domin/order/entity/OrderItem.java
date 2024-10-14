package org.c4marathon.assignment.domin.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.c4marathon.assignment.domin.item.entity.Item;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private Integer orderPrice;

    private Integer orderQuantity;

    @Builder
    public OrderItem(Item item, Integer orderPrice, Integer orderQuantity) {
        this.item = item;
        this.orderPrice = orderPrice;
        this.orderQuantity = orderQuantity;
    }
}
