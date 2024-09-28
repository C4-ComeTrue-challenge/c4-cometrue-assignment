package org.c4marathon.assignment.member.domain;

import jakarta.persistence.*;
import lombok.Getter;
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
    @Getter
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false, unique = true, length = 20)
    private String nickname;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private Customer(final Long memberId, final String nickname) {
        this.memberId = memberId;
        this.nickname = nickname;
    }

    public static Customer of(Long memberId, String nickname) {
        return new Customer(memberId, nickname);
    }
}
