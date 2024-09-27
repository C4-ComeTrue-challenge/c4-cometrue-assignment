package org.c4marathon.assignment.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Customer {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String nickname;

    @Column(nullable = false, unique = true, length = 40)
    private String email;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private Customer(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }

    public Customer of(String nickname, String email) {
        return new Customer(nickname, email);
    }
}
