package org.c4marathon.assignment.member.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import static org.c4marathon.assignment.member.domain.MemberAuthority.CUSTOMER;
import static org.c4marathon.assignment.member.domain.MemberAuthority.MERCHANT;

@Entity
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @Getter
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column
    private Long merchantId;

    @Column
    private Long customerId;

    @Column(nullable = false, unique = true, length = 20)
    private String nickname;

    @Getter
    @Column(nullable = false)
    private String password;

    @Getter
    @Enumerated(STRING)
    private MemberAuthority authority;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private Member(Long userId, String nickname, MemberAuthority authority, String password) {
        if (authority == CUSTOMER) {
            this.customerId = userId;
        } else {
            this.merchantId = userId;
        }
        this.nickname = nickname;
        this.authority = authority;
        this.password = password;
    }

    public static Member Customer(Long userId, String nickname, String password) {
        return new Member(userId, nickname, CUSTOMER, password);
    }

    public static Member Merchant(Long userId, String nickname, String password) {
        return new Member(userId, nickname, MERCHANT, password);
    }
}
