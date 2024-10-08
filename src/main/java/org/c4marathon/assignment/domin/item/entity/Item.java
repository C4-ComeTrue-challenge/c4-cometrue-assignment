package org.c4marathon.assignment.domin.item.entity;

import jakarta.persistence.*;
import lombok.*;
import org.c4marathon.assignment.domin.order.controller.OrderErrorStatus;
import org.c4marathon.assignment.domin.user.entity.User;
import org.c4marathon.assignment.global.common.domain.BaseEntity;
import org.c4marathon.assignment.global.exception.GeneralException;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    private Integer price;

    private Integer stock;

    private String description;

    public void decreaseStock(int quantity) {
        this.stock -= quantity;
    }
}
