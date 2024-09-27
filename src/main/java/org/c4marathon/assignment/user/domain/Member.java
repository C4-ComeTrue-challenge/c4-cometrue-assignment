package org.c4marathon.assignment.user.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import static org.c4marathon.assignment.user.domain.MemberAuthority.CUSTOMER;
import static org.c4marathon.assignment.user.domain.MemberAuthority.MERCHANT;

@Entity
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column
    private Long merchantId;

    @Column
    private Long customerId;

    @Column(nullable = false, unique = true, length = 20)
    private String nickname;

    @Column(nullable = false, unique = true, length = 40)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(STRING)
    private MemberAuthority authority;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    private Member(Long userid, String nickname, MemberAuthority authority,
                   String email, String password) {
        if (authority == CUSTOMER) {
            this.customerId = userid;
        } else {
            this.merchantId = userid;
        }
        this.nickname = nickname;
        this.authority = authority;
        this.email = email;
        this.password = password;
    }

    public Member Customer(Long userid, String nickname,
                           String email, String password) {
        return new Member(userid, nickname, CUSTOMER, email, password);
    }

    public Member Merchant(Long userid, String nickname,
                           String email, String password) {
        return new Member(userid, nickname, MERCHANT, email, password);
    }
}
