package org.c4marathon.assignment.domin.item.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.c4marathon.assignment.domin.user.entity.User;
import org.c4marathon.assignment.global.common.domain.BaseEntity;

@Entity
@Getter
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

    @Builder
    public Item(User user, String name, Integer price, Integer stock, String description) {
        this.user = user;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.description = description;
    }
}
