package org.c4marathon.assignment.domin.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.c4marathon.assignment.global.common.domain.BaseEntity;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public User(String email, String password, String name, Role role, String account, String bank) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.cache = 0;
        this.point = 0;
        this.account = account;
        this.bank = bank;
    }

    @Builder
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
