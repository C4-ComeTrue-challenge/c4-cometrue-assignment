package org.c4marathon.assignment.domin.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.c4marathon.assignment.global.common.domain.BaseEntity;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Integer cache;

    private Integer point;

    private String account;

    private String bank;

    public void decreaseCache(int totalPrice) {
        this.cache -= totalPrice;
    }

    public void increaseCache(int totalPrice) {
        this.cache += totalPrice;
    }

    public void updateCache(int amount) {
        this.cache += amount;
    }

}
